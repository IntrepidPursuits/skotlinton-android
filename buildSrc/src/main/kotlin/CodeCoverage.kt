import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

object CodeCoverage {
    const val JACOCO_VERSION = "0.8.4"

    val generatedClasses = setOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "com/android/**/*.class",
            "**/*_ViewBind*" // ButterKnife auto generated classes
    )

    val androidClasses = setOf(
            "**/*Activity.class",
            "**/*Activity$*.class",
            "**/*Fragment.class",
            "**/*Fragment$*.class",
            "**/*Service.class",
            "**/*Service$*.class",
            "**/*Application.class",
            "**/*Application$*.class",
            "**/*Dialog.class",
            "**/*Dialog$*.class",
            "**/*Adapter.class",
            "**/*Adapter$*.class",
            "**/*ViewHolder.class",
            "**/*ViewHolder$*.class",
            "**/*Animation.class",
            "**/*Animation$*.class"
    )

    /***
     * Creates jacoco tasks and configures various extensions to allow for test coverage
     * The generated tasks are in the format of
     * `{coverageReport|coverageMinimum}{$buildVariant}{Unit|Ui|Combined}Test`
     * The `coverageReport` tasks simply generates the coverage report, whereas `coverageMinimum` also checks to make
     * sure the coverage percentages is above the minimum threshold and fails the build if it isn't
     * `Unit` test only runs the JVM unitTests, `Ui` test only runs the Android Instrumentation tests (currently through
     * Spoon), and `Combined` test runs both.
     * ex `./gradlew coverageReportDebugUnitTest` generates unit test coverage report for the debug build
     *
     * @param project gradle project, usually `this` when called from the module build.gradle file
     * @param minimumCombinedCoverage failure threshold when running coverageMinimum${variant}CombinedTest
     * @param minimumUnitCoverage failure threshold when running coverageMinimum${variant}UniTest
     * @param minimumUiCoverage failure threshold when running coverageMinimum${variant}UiTest
     * @param commonExcludedClasses classes that are excluded from all test coverages. Typically for generated classes
     * @param unitTestExcludedClasses classes that are excluded only from unit tests. Typically for Android classes
     * @param uiTestExcludedClasses classes that are excluded only from UI tests
     */
    fun configure(
            project: Project,
            minimumCombinedCoverage: Double = 0.7,
            minimumUnitCoverage: Double = 0.5,
            minimumUiCoverage: Double = 0.6,
            commonExcludedClasses: Set<String> = generatedClasses,
            unitTestExcludedClasses: Set<String> = androidClasses,
            uiTestExcludedClasses: Set<String> = emptySet()
    ) {
        with(project) {
            plugins.apply("jacoco")

            configure<JacocoPluginExtension> {
                toolVersion = JACOCO_VERSION
            }

            tasks.withType(Test::class) {
                // needed so that Robolectric tests are included in the results
                extensions.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                }
            }

            configure<BaseExtension> {
                buildTypes {
                    getByName("debug") {
                        // testCoverageEnabled messes up debugging/etc, so we want it disabled most of times.
                        // unit test coverage reports seems to work fine even with the flag disabled, but the
                        // instrumentation tests need this flag otherwise the coverage data may be incomplete
                        isTestCoverageEnabled = isRunningUiTestCoverage
                    }
                }
            }
        }

        project.afterEvaluate {
            allVariants(project)?.forEach {
                configureVariant(
                        it,
                        project,
                        minimumCombinedCoverage,
                        minimumUnitCoverage,
                        minimumUiCoverage,
                        commonExcludedClasses,
                        unitTestExcludedClasses,
                        uiTestExcludedClasses)
            }
        }
    }

    private fun configureVariant(variant: BaseVariant,
                                 project: Project,
                                 minimumCombinedCoverage: Double,
                                 minimumUnitCoverage: Double,
                                 minimumUiCoverage: Double,
                                 commonExcludedClasses: Set<String>,
                                 unitTestExcludedClasses: Set<String>,
                                 uiTestExcludedClasses: Set<String>
    ) {
        val variantNameCapitalized = variant.name.capitalize()
        val variantNameDecapitalized = variant.name.decapitalize()
        val buildDir = project.buildDir

        fun createJacocoTasks(testType: String,
                              testTasks: Array<String>,
                              minimumCoverage: Double,
                              excludedClasses: Set<String>,
                              ecData: ConfigurableFileCollection) {
            val reportTaskName = "coverageReport${variantNameCapitalized}${testType}Test"
            val coverageSourceDirs = project.files("src/main/kotlin")
            val coverageClassDirs = project.fileTree("$buildDir/tmp/kotlin-classes/$variantNameDecapitalized") {
                setExcludes(excludedClasses)
            }

            project.tasks.register<JacocoReport>(reportTaskName) {
                dependsOn(testTasks)
                group = "verification"

                sourceDirectories.setFrom(coverageSourceDirs)
                classDirectories.setFrom(coverageClassDirs)
                executionData.setFrom(ecData)

                val reportLocationPrefix = "$buildDir/reports/jacoco/$reportTaskName"
                reports {
                    xml.isEnabled = true
                    xml.destination = project.file("$reportLocationPrefix/jacoco.xml")
                }

                doLast {
                    println("You can view the coverage report at:")
                    println("$reportLocationPrefix/html/index.html")
                }
            }

            project.tasks.register<JacocoCoverageVerification>("coverageMinimum$variantNameCapitalized${testType}Test") {
                dependsOn(reportTaskName)
                group = "verification"

                sourceDirectories.setFrom(coverageSourceDirs)
                classDirectories.setFrom(coverageClassDirs)
                executionData.setFrom(ecData)

                violationRules {
                    rule {
                        limit {
                            minimum = minimumCoverage.toBigDecimal()
                        }
                    }
                }
            }
        }

        val unitTestExecutionData = "$buildDir/jacoco/test${variantNameCapitalized}UnitTest.exec"
        createJacocoTasks(
                testType = "Unit",
                testTasks = arrayOf("test${variantNameCapitalized}UnitTest"),
                minimumCoverage = minimumUnitCoverage,
                excludedClasses = commonExcludedClasses + unitTestExcludedClasses,
                ecData = project.files(unitTestExecutionData)
        )

        val uiTestExecutionData = "$buildDir/spoon-output/$variantNameDecapitalized/coverage/merged-coverage.ec"
        createJacocoTasks(
                testType = "Ui",
                testTasks = arrayOf("spoon${variantNameCapitalized}AndroidTest"),
                minimumCoverage = minimumUiCoverage,
                excludedClasses = commonExcludedClasses + uiTestExcludedClasses,
                ecData = project.files(uiTestExecutionData)
        )

        createJacocoTasks(
                testType = "Combined",
                testTasks = arrayOf("test${variantNameCapitalized}UnitTest", "spoon${variantNameCapitalized}AndroidTest"),
                minimumCoverage = minimumCombinedCoverage,
                excludedClasses = commonExcludedClasses,
                ecData = project.files(unitTestExecutionData, uiTestExecutionData)
        )
    }
}

val Project.isRunningUiTestCoverage: Boolean
    get() {
        return gradle.startParameter.taskNames.any {
            (it.startsWith("coverageReport") || it.startsWith("coverageMinimum")) &&
                    (it.endsWith("UiTest") || it.endsWith("CombinedTest"))
        }
    }

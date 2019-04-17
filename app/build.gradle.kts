import com.android.builder.internal.ClassFieldImpl

plugins {
    id("com.android.application")
    id("spoon")
    id("kotlin-android")
    id("kotlin-kapt")
    id("io.intrepid.static-analysis")
    // Uncomment the following line after adding fabric.properties file
    //id("io.fabric")

    id("jacoco")
}

android {
    compileSdkVersion(28)
    buildToolsVersion = "28.0.3"

    defaultConfig {
        applicationId = "io.intrepid.skotlinton"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "io.intrepid.skotlinton.CustomTestRunner"

        // these are debug configs but are placed here instead of the debug block to avoid the "value is being replaced" warning
        buildConfigField("boolean", "LOG_CONSOLE", "true")
        buildConfigField("boolean", "REPORT_CRASH", "false")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "password"
            keyAlias = "debug"
            keyPassword = "password"
        }

        create("release") {
            val fileName = project.getLocalProperty("signing_file")
            if (!fileName.isEmpty()) {
                storeFile = file(fileName)
                storePassword = project.getLocalProperty("signing_password")
                keyAlias = project.getLocalProperty("signing_alias")
                keyPassword = project.getLocalProperty("signing_key_password")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"

            buildConfigField("boolean", "LOG_CONSOLE", "true")
            buildConfigField("boolean", "REPORT_CRASH", "false")
        }

        create("qa") {
            initWith(buildTypes.getByName("debug"))

            applicationIdSuffix = ".qa"
            versionNameSuffix = Version.versionSuffix()
        }

        getByName("release") {
            buildConfigField("boolean", "LOG_CONSOLE", "false")
            buildConfigField("boolean", "REPORT_CRASH", "true")

            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    val qaImplementation by configurations

    implementation(fileTree("libs") { include("*.jar") })

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Dependencies.kotlin}")

    implementation("com.crashlytics.sdk.android:crashlytics:2.9.8")

    // Support libraries
    val supportAnnotationLib = "androidx.annotation:annotation:1.0.1"
    implementation(supportAnnotationLib)
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("com.google.android.material:material:1.0.0")

    // architecture components
    val lifecycleVersion = "2.0.0"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion")

    // Google play services
    val googlePlayVersion = "16.1.0"
    implementation("com.google.android.gms:play-services-base:$googlePlayVersion")

    // Rx
    implementation("io.reactivex.rxjava2:rxjava:2.2.5")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.0")
    implementation("com.jakewharton.rxbinding2:rxbinding-kotlin:2.2.0")

    // Jake Wharton/Square
    val retrofitVersion = "2.5.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.google.code.gson:gson:2.8.5")

    val okhttpVersion = "3.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    val butterKnifeVersion = "10.0.0"
    implementation("com.jakewharton:butterknife:$butterKnifeVersion")
    kapt("com.jakewharton:butterknife-compiler:$butterKnifeVersion")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.squareup.picasso:picasso:2.71828")

    // Other
    implementation("io.intrepid.commonutils:commonutils:0.2.3")

    // LeakCanary
    val leakCanaryVersion = "1.6.3"
    debugImplementation("com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion")
    releaseImplementation("com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion")
    qaImplementation("com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion")
    testImplementation("com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion")

    // Common test dependencies
    val junitLib = "junit:junit:4.12"
    val mockitoVersion = "2.23.4"
    val mockitoLib = "org.mockito:mockito-core:$mockitoVersion"
    val mockitoKotlinLib = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0"
    val kluentLib = "org.amshove.kluent:kluent-android:1.46"

    // Unit tests
    testImplementation(junitLib)
    testImplementation(mockitoLib)
    testImplementation(supportAnnotationLib)
    testImplementation(mockitoKotlinLib)
    testImplementation(kluentLib)
    // need to explicitly declare this as AS 3.3 somehow can't run the test without this
    // https://github.com/MarkusAmshove/Kluent/issues/130
    testImplementation("org.jetbrains.kotlin:kotlin-test:${Dependencies.kotlin}")
    testImplementation("androidx.arch.core:core-testing:2.0.1")

    // UI tests
    androidTestImplementation(junitLib)
    androidTestImplementation(mockitoLib)
    androidTestImplementation(supportAnnotationLib)
    androidTestImplementation(mockitoKotlinLib) {
        exclude(module = "kotlin-reflect")
    }
    androidTestImplementation(kluentLib)
    androidTestImplementation("org.mockito:mockito-android:$mockitoVersion")

    androidTestImplementation("androidx.test:core:1.1.0")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test:rules:1.1.1")
    val espressoVersion = "3.1.0"
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:$espressoVersion")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:$okhttpVersion")

    // Forcing to use an older version right now since newer versions have issues with jetifier
    // https://github.com/raphw/byte-buddy/issues/541
    androidTestImplementation("net.bytebuddy:byte-buddy:1.8.22") {
        isForce = true
    }
    androidTestImplementation("net.bytebuddy:byte-buddy-agent:1.8.22") {
        isForce = true
    }
}

spoon {
    debug = true
    adbTimeout = 30
    grantAll = true

    (project.findProperty("spoonClassName") as? String)?.let {
        className = it
    }
    (project.findProperty("spoonMethodName") as? String)?.let {
        methodName = it
    }
}

// region test coverage
// the following section should have been in a different file. However, currently gradle only allows specifying
// the plugin{} block in the main build.gradle file, so the configurations have to live here as well.
// https://docs.gradle.org/current/userguide/plugins.html#sec:binary_plugins mentioned that the restriction will
// be removed in the future
jacoco {
    toolVersion = "0.8.2"
}

// change the buildType and productFlavor fields here if the project uses different build variant for testing
val buildType = "debug"
val productFlavor = ""
val buildVariant = "${productFlavor.capitalize()}${buildType.capitalize()}"
val buildVariantDirectory = "$productFlavor/$buildType"

val coverageSourceDirs = files("src/main/kotlin")
val coverageClassDirs = fileTree("$buildDir/tmp/kotlin-classes/$buildVariantDirectory") {
    setExcludes(setOf("**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "com/android/**/*.class",
            "**/*_ViewBind*" // ButterKnife auto generated classes
    ))
}
val unitTestCoverageData = "$buildDir/jacoco/test${buildVariant}UnitTest.exec"
val instrumentationTestCoverageData = "$buildDir/spoon-output/$buildVariantDirectory/coverage/merged-coverage.ec"

// testCoverageEnabled messes up debugging/etc, so we want it disabled most of times.
// unit test coverage reports seems to work fine even with the flag disabled, but the
// instrumentation tests need this flag otherwise the coverage data may be incomplete
val startTaskNames: List<String> = gradle.startParameter.taskNames
val isRunningInstrumentationCoverage = startTaskNames.contains("instrumentationTestCoverage") || startTaskNames.contains("testCoverage")

// Run both unit and instrumentation tests and merge the result of both tests into a single report
tasks.register<JacocoReport>("testCoverage") {
    dependsOn("test${buildVariant}UnitTest", "spoon${buildVariant}AndroidTest")

    sourceDirectories.setFrom(coverageSourceDirs)
    classDirectories.setFrom(coverageClassDirs)
    executionData.setFrom(files(unitTestCoverageData, instrumentationTestCoverageData))

    doLast {
        println("You can view the coverage report at:")
        println("$buildDir/reports/jacoco/testCoverage/html/index.html")
    }

    reports {
        xml.isEnabled = true
        xml.destination = file("$buildDir/reports/jacoco/testCoverage/jacoco.xml")
    }
}

// Run and generate the report for the unit tests. The test report only includes the coverage data for
// the view model and utils classes since we generally don't unit test any Android related classes
tasks.register<JacocoReport>("unitTestCoverage") {
    dependsOn("test${buildVariant}UnitTest")

    sourceDirectories.setFrom(coverageSourceDirs)
    classDirectories.setFrom(fileTree("$buildDir/tmp/kotlin-classes/$buildVariantDirectory") {
        setExcludes(coverageClassDirs.excludes + setOf(
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
        ))
    })
    executionData.setFrom(files("$buildDir/jacoco/test${buildVariant}UnitTest.exec"))

    doLast {
        println("You can view the coverage report at:")
        println("$buildDir/reports/jacoco/unitTestCoverage/html/index.html")
    }

    reports {
        xml.isEnabled = true
        xml.destination = file("$buildDir/reports/jacoco/unitTestCoverage/jacoco.xml")
    }
}

// Run and generate the report for the instrumentation test
tasks.register<JacocoReport>("instrumentationTestCoverage") {
    dependsOn("spoon${buildVariant}AndroidTest")

    sourceDirectories.setFrom(coverageSourceDirs)
    classDirectories.setFrom(coverageClassDirs)
    executionData.setFrom(files(instrumentationTestCoverageData))

    doLast {
        println("You can view the coverage report at:")
        println("$buildDir/reports/jacoco/instrumentationTestCoverage/html/index.html")
    }

    reports {
        xml.isEnabled = true
        xml.destination = file("$buildDir/reports/jacoco/instrumentationTestCoverage/jacoco.xml")
    }
}

android {
    buildTypes {
        getByName("debug") {
            isTestCoverageEnabled = isRunningInstrumentationCoverage
        }
    }
}

tasks.withType(Test::class) {
    // needed so that Robolectric tests are included in the results
    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
    }
}

spoon {
    codeCoverage = isRunningInstrumentationCoverage
}
// endregion

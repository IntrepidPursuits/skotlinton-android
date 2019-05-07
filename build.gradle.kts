// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven("https://maven.fabric.io/public")
        maven("https://plugins.gradle.org/m2/")
        maven("https://oss.sonatype.org/content/repositories/snapshots") // TODO remove this once spoon 2.0.0 is stable
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.kotlin}")
        classpath("com.jaredsburrows:gradle-spoon-plugin:1.3.0")
        classpath("com.squareup.spoon:spoon-runner:2.0.0-SNAPSHOT") // TODO update/remove this once spoon 2.0.0 is stable
        classpath("io.fabric.tools:gradle:1.26.1")
        classpath("gradle.plugin.io.intrepid:static-analysis:1.2.2")
    }
}

apply<JacocoPlugin>()

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://maven.fabric.io/public")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

// Top level tasks that should be run by CI
val verificationTasks = listOf(
        "app:coverageMinimumDebugUnitTest",
        "app:lintDebug"
)
tasks.register("prCheck") {
    val tasks = verificationTasks + listOf(
            "app:assembleDebug"
    )
    dependsOn(tasks)
    group = "verification"
    description = "Includes all the tasks that should be run during a PR check"
}
tasks.register("continuousBuild") {
    val tasks = verificationTasks + listOf(
            "app:assembleQa"
    )
    dependsOn(tasks)
    group = "build"
    description = "Includes all the tasks that should be run on the main branch once a PR is merged in"
}

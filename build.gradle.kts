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

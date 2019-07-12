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

//https://github.com/mockk/mockk/issues/281
configurations.all {
    resolutionStrategy {
        force("org.objenesis:objenesis:2.6")
    }
}

dependencies {
    val qaImplementation by configurations

    implementation(fileTree("libs") { include("*.jar") })

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Dependencies.kotlin}")

    val coroutineVersion = "1.3.0-M1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

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

    // Jake Wharton/Square
    val retrofitVersion = "2.6.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
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
    val mockkVersion = "1.9.3"
    val kluentLib = "org.amshove.kluent:kluent-android:1.46"

    // Unit tests
    testImplementation(junitLib)
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation(supportAnnotationLib)
    testImplementation(kluentLib)
    // need to explicitly declare this as AS 3.3 somehow can't run the test without this
    // https://github.com/MarkusAmshove/Kluent/issues/130
    testImplementation("org.jetbrains.kotlin:kotlin-test:${Dependencies.kotlin}")
    testImplementation("androidx.arch.core:core-testing:2.0.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")

    // UI tests
    androidTestImplementation(junitLib)
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation(supportAnnotationLib)
    androidTestImplementation(kluentLib)

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

    codeCoverage = isRunningUiTestCoverage
}

CodeCoverage.configure(this)

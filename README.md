# Skotlinton

[![Build Status](https://ci.intrepid.io/buildStatus/icon?job=Android/android-projects/skotlinton-android)](https://ci.intrepid.io/job/Android/job/android-projects/job/skotlinton-android/)
[![Coverage](http://ci.intrepid.io:9913/jenkins/cobertura/Android/job/android-projects/job/skotlinton-android/)](https://ci.intrepid.io/job/Android/job/android-projects/job/skotlinton-android/cobertura/)

Kotlin port of [Android Skeleton](https://github.com/IntrepidPursuits/skeleton-android) project.

1. [Overview](#overview)
1. [Cloning the Project](#cloning-the-project)
1. [Building](#building)
    1. [Configurations](#configurations)
        1. [Release](#release)
    1. [Jenkins](#jenkins)
1. [Testing](#testing)
    1. [Unit Tests](#unit-tests)
    1. [UI/Instrumentation Tests](#uiinstrumentation-tests)
    1. [Code Coverage](#code-coverage)
1. [Architecture](#architecture)
    1. [Model-View-ViewModel](#model-view-viewmodel)
    1. [Base Classes](#base-classes)
    1. [Third Party Libraries](#third-party-libraries)
1. [History](#history)

## Overview
The project contains the following components:

-   Commonly used third party dependencies (support library, RxJava, Jake Wharton Experience, etc)
-   Gradle configuration (build types, signing configs, retrolambda)
-   Base MVVM framework (includes a few examples and how they are used in tests)
-   Various other setups that we often do when starting projects (tests, custom Application, Retrofit, Timber, Crashlytics, common strings, etc)

## Cloning the project
To start a new project that's based on this project, simply download/clone the repo and then rename all instances of "skotlinton" to your project name. You can use the [nekromancer.sh](./nekromancer.sh) to automate this

The script takes in the following arguments
```
-p  Specifies the the package name of the new app. The package name
    should be in the form of x.y.z . The last part of the package name
    will also serve as the application name and the directory name.
-d  Specifies the directory that the project will be downloaded/cloned
    to. Defaults to the current directory if it's not specified.
```

example usage:
```
./nekromancer.sh -p io.intrepid.zombie -d ~/AndroidStudioProjects/
```

## Building
This project does not require any additional setup or special configurations to build or run.

### Configurations
- There are 3 different build types - Debug, QA, and Release.
- Both Debug and QA builds are debuggable and logs to console. The main difference between them is that QA builds also report to Crashlytics.
- Release builds are not debuggable and don't show console logs, but they report to Crashlytics.

Each build has different application and name suffix, so they can be installed side by side on a phone.

#### Release
To configure the release builds, generate the keystore file and add the following lines to the `local.properties` file:
```
signing_file=../release.keystore
signing_alias=#####
signing_key_password=#####
signing_password=#####
```
(replace ##### with the actual credentials)

### Jenkins
There are 2 different Jenkins builds for this project:
- skotlinton-android - This is run every time something is merged into the develop branch. It runs the unit tests, generates a QA build, and uploads it to the OTA link.
- skotlinton-android-pr - This is run every time a PR is created. It runs the unit tests and reports the result back to the GitHub PR page.

Jenkins will run `prCheck` gradle task when checking PRs, and `continuousBuild` task on the main branch after the PR is merged. You can run these tasks locally to ensure the code pass all checks before submitting the PR.

## Testing
### Unit Tests
Unit tests exist under the "test" directory. They can be run using the standard commands. ex. `./gradlew testDebugUnitTest`

### UI/Instrumentation Tests
UI (Espresso) tests exist under the "androidTest" directory. The project also uses [Spoon](https://github.com/square/spoon) and its [gradle plugin](https://github.com/stanfy/spoon-gradle-plugin) to run instrumentation tests and generate a easy to read report. To run an instrumentation test, use `./gradlew spoon`.

### Code Coverage
Code coverage configurations are handled by [CodeCoverage.kt](buildSrc/src/main/kotlin/CodeCoverage.kt). To generate a code coverage report, use `./gradlew coverageReportDebugCombinedTest`. This will run both unit and instrumentation tests and merge the result of both tests into a single report. To enforce minimum code coverage, run `./gradlew coverageMinimumDebugCombinedTest`. Replace `Combined` with `Unit` or `Ui` if you just want to run one type of test.

## Architecture
### Model-View-ViewModel
The app uses the popular MVVM architecture to allow for separation of logic and ease of testing. In this paradigm, all business logic should live inside viewmodel (but they can delegate some tasks to other classes that are injected as dependencies). Activities and fragment will act as "views", they should not have any logic other than passing the user events to the view model and displaying the data. ViewModels will expose view states as LiveData which the View can observe.

### Base Classes
- `BaseActivity`: Base class for all activities. Includes lifecycle logging and view inflation.
- `BaseMvvmActivity`: Base class for activities that will have some business logic instead of just hosting a fragment. Includes setup for creating view models.
- `BaseFragmentActivity`: Base class for activities whose sole purpose to to host a fragment. If the activity contains any additional logic, use `BaseMvvmActivity` instead.
- `BaseFragment`: Basically the same as `BaseMvvmActivity`, but for fragments.
- `BaseViewModel`: Base class for all view models. Includes lifecycle setup and common dependencies. Goes along with `BaseMvvmActivity` and `BaseFragment`.
- `ViewModelConfiguration`: Wrapper class for common dependencies that all view models are expected to have.

### Third Party Libraries
- RxJava/RxAndroid (MVVM view binding and asynchronous event handling)
- ButterKnife (easier view binding)
- Retrofit/OkHttp (simplifies network requests)
- Timber (better logging tool)
- Picasso (simplifies image loading)
- LeakCanary (helps catch memory leaks)
- Crashlytics (reports crashes)
- Mockito (mocks out classes for tests)
- Espresso (UI testing framework)
- Spoon (displays the test results in an easy to read format)

## History

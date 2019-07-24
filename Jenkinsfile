pipeline {
    agent { label 'android-docker' }
    options {
        timeout(time: 1, unit: 'HOURS')
    }

    stages {
        stage('PR') {
            when { changeRequest() }
            steps {
                gradlew(args: ['prCheck'])
            }
        }

        stage('Build') {
            when {
                not { changeRequest() }
            }
            steps {
                gradlew(args: ['continuousBuild'], name: 'Skotlinton-QA')
            }
        }

        stage('Deploy') {
            agent { label 'master' }
            when {
                not { changeRequest() }
            }
            steps {
                script {
                    ota.publishAPK(name: 'Skotlinton-QA')
                }
            }
        }

        stage('Metrics') {
            when {
                not { changeRequest() }
            }
            steps {
                androidLint(pattern: '**/lint-results*.xml')
                junit(allowEmptyResults: false, testResults: '**/test-results/*/*.xml')
                publishCoverage(adapters: [jacocoAdapter('app/build/reports/jacoco/coverageReportDebugUnitTest/jacoco.xml')], sourceFileResolver: sourceFiles('STORE_LAST_BUILD'))
            }
        }
    }
}

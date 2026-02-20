pipeline {
    tools{
        maven 'Default'
    }
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                // Clean and package the application, skipping tests initially
                sh 'mvn clean install -B'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test -B -Dtest=ErrorHistoryTest,DatabaseMigrationHistoryTest'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/*.jar'
        }
    }
}

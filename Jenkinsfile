pipeline {
    tools{
        maven 'Default'
    }
    agent any
    options {
        skipStagesAfterUnstable()
    }

    stages {
        stage('Checkout Source Code') {
                steps {
                    checkout scm
                }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests -B'
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
            archiveArtifacts artifacts: 'target/*.jar'
        }
    }
}

pipeline {
    tools{
        maven 'Default'
    }
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stage('Checkout Source Code') {
        steps {
            checkout scm
        }
    }

    stages {
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

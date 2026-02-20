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
        stage('Test') {
            steps {
                sh 'mvn test -B -Dtest=ErrorHistoryTest,DatabaseMigrationHistoryTest'
            }
        }
        stage('Build') {
                    steps {
                        sh 'mvn clean install -DskipTests -B'
                    }
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

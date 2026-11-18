pipeline {
    agent any

    tools {
        maven 'maven-3'
    }

    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
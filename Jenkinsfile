pipeline {
    agent any

    tools {
        maven 'maven-3'
    }

        environment {
            EC2_HOST = '43.205.210.126'
        }

    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'SSH_KEY')]) {
                    sh 'ssh -i $SSH_KEY -o StrictHostKeyChecking=accept-new ec2-user@$EC2_HOST sudo systemctl stop http-request-demo'
                    sh 'scp -i $SSH_KEY target/http-request-demo-0.0.1-SNAPSHOT.jar ec2-user@$EC2_HOST:/home/ec2-user/'
                    sh 'ssh -i $SSH_KEY ec2-user@$EC2_HOST sudo systemctl start http-request-demo'
                }
            }
        }
    }
}
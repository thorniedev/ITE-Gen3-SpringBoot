pipeline {
    agent any

    environment {
        SERVER_HOST = "18.142.29.184"
        SERVER_USER = "ubuntu"
        SERVER_PATH = "/home/ubuntu/app/ite-commerce"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Ecommerce') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean :ecommerce:bootJar -x test --no-daemon'
            }
        }

        stage('Copy Files to Server') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${SERVER_USER}@${SERVER_HOST} 'mkdir -p ${SERVER_PATH}'
                    scp ecommerce/build/libs/*.jar ${SERVER_USER}@${SERVER_HOST}:${SERVER_PATH}/app.jar
                    scp docker-compose.prod.yml ${SERVER_USER}@${SERVER_HOST}:${SERVER_PATH}/docker-compose.yml
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh """
                    ssh ${SERVER_USER}@${SERVER_HOST} '
                    cd ${SERVER_PATH} &&
                    docker compose down &&
                    docker compose up -d --build
                    '
                    """
                }
            }
        }
    }
}
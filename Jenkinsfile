pipeline {
    agent any

    environment {
        IMAGE_NAME = "ghcr.io/thorniedev/ite-gen3-springboot-ecommerce"
        SERVER_HOST = "18.142.29.184"
        SERVER_USER = "ubuntu"
        SERVER_PATH = "/home/ubuntu/app/ite-commerce"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                        currentBuild.displayName = "v1.0.${env.BUILD_NUMBER}"
                }
                checkout scm
            }
        }

        stage('Build Ecommerce') {
           steps {
              sh 'chmod +x ./gradlew'
              sh './gradlew clean :ecommerce:bootJar -x test --no-daemon'
           }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                echo $PATH
                /usr/bin/docker --version

                /usr/bin/docker build \
                  -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                  -t ${IMAGE_NAME}:latest \
                  ./ecommerce
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'ghcr-login',
                    usernameVariable: 'GHCR_USER',
                    passwordVariable: 'GHCR_TOKEN'
                )]) {
                    sh '''
                    echo "$GHCR_TOKEN" | /usr/bin/docker login ghcr.io -u "$GHCR_USER" --password-stdin
                    /usr/bin/docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                    /usr/bin/docker push ${IMAGE_NAME}:latest
                    '''
                }
            }
        }

        stage('Copy Compose File') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ${SERVER_USER}@${SERVER_HOST} "mkdir -p ${SERVER_PATH}"
                    scp docker-compose.prod.yml ${SERVER_USER}@${SERVER_HOST}:${SERVER_PATH}/docker-compose.yml
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'ghcr-login',
                    usernameVariable: 'GHCR_USER',
                    passwordVariable: 'GHCR_TOKEN'
                )]) {
                    sshagent(['ec2-ssh-key']) {
                        sh '''
                        ssh ${SERVER_USER}@${SERVER_HOST} "
                            echo '${GHCR_TOKEN}' | docker login ghcr.io -u '${GHCR_USER}' --password-stdin &&
                            cd ${SERVER_PATH} &&
                            docker-compose down &&
                            docker-compose pull &&
                            docker-compose up -d
                        "
                        '''
                    }
                }
            }
        }
    }
}
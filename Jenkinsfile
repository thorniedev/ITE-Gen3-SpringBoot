pipeline {
    agent any

    environment {
        IMAGE_NAME = "ghcr.io/thorniedev/ite-gen3-springboot-ecommerce"
        SERVER_HOST = "18.142.29.184"
        SERVER_USER = "ubuntu"
        SERVER_PATH = "/home/ubuntu/app/ite-commerce"
        LOCAL_COMPOSE_FILE = "docker-compose.prod.yml"
        REMOTE_COMPOSE_FILE = "docker-compose.prod.yml"
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
                    scp ${LOCAL_COMPOSE_FILE} ${SERVER_USER}@${SERVER_HOST}:${SERVER_PATH}/${REMOTE_COMPOSE_FILE}
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
                            set -e

                            if docker compose version >/dev/null 2>&1; then
                                compose_cmd='docker compose'
                            elif command -v docker-compose >/dev/null 2>&1; then
                                compose_cmd='docker-compose'
                            else
                                echo 'Docker Compose is not installed on the server.' >&2
                                echo 'Install Compose v2 or docker-compose v1, then rerun Jenkins.' >&2
                                exit 1
                            fi

                            echo '${GHCR_TOKEN}' | docker login ghcr.io -u '${GHCR_USER}' --password-stdin
                            cd ${SERVER_PATH}
                            test -f .env

                            ECOMMERCE_IMAGE=${IMAGE_NAME}:${BUILD_NUMBER} eval "\\$compose_cmd -f ${REMOTE_COMPOSE_FILE} pull ecommerce"
                            ECOMMERCE_IMAGE=${IMAGE_NAME}:${BUILD_NUMBER} eval "\\$compose_cmd -f ${REMOTE_COMPOSE_FILE} up -d"
                        "
                        '''
                    }
                }
            }
        }
    }
}

pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Clonando repositorio monitoramento-qualidade...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compilando monitoramento-qualidade com Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Gerando imagem Docker do monitoramento-qualidade...'
                sh 'docker build -t vinheria/monitoramento-qualidade:latest .'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Realizando deploy do monitoramento-qualidade...'
                // Para o container antigo se estiver rodando
                sh 'docker stop monitoramento-qualidade || true'
                sh 'docker rm monitoramento-qualidade || true'
                // Sobe o novo container
                sh '''
                    docker run -d \
                        --name monitoramento-qualidade \
                        --network vinheria-network \
                        -p 8084:8084 \
                        vinheria/monitoramento-qualidade:latest
                '''
                echo 'Deploy do monitoramento-qualidade concluido! Rodando na porta 8084.'
            }
        }

    }

    post {
        success {
            echo 'Pipeline do monitoramento-qualidade finalizado com sucesso!'
        }
        failure {
            echo 'Algo deu errado no pipeline do monitoramento-qualidade.'
        }
    }
}

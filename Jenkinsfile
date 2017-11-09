pipeline {
    agent any
    stages {
        stage('Project Documentation') {
            steps {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                sh "./documentation/paper/make"
            }
        }
    }
}

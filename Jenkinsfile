
// Documentation step varibales
DOCUMENT_NAME = "main"
DOCUMENTATION_DIR = "./documentation/paper"

pipeline {
    agent any
    stages {
        stage('build documentation') {
            steps {

                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                dir (DOCUMENTATION_DIR) {
                	sh "pdflatex  " + DOCUMENT_NAME + ".tex"
                	sh "biber  " + DOCUMENT_NAME
                	sh "pdflatex  " + DOCUMENT_NAME + ".tex"
            	}
            }
        }
    }
    post {
    	always {
    		archiveArtifacts artifacts: "**/" + DOCUMENT_NAME + ".pdf", fingerprint: true
    	}
    }
}

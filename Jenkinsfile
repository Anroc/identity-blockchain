// These 2 need to be authorized using jenkins script approval
// http://your.jenkins.host/scriptApproval/
import groovy.json.JsonOutput

// Add whichever params you think you'd most want to have
// replace the slackURL below with the hook url provided by
// slack when you configure the webhook

def notifySlack(text, channel, attachments) {

    //your  slack integration url
    def slackURL = ' https://hooks.slack.com/services/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' 
    //from the jenkins wiki, you can updload an avatar and
    //use that one
    def jenkinsIcon = 'https://wiki.jenkins-ci.org/download/attachments/327683/JENKINS?version=1&modificationDate=1302750804000'
    
    def payload = JsonOutput.toJson([text      : text,
                                     channel   : channel,
                                     username  : "jenkins",
                                     icon_url: jenkinsIcon,
                                     attachments: attachments
                                     ])
                                     
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
}

def slackPrepare(String buildStatus = 'STARTED') {
    // Build status of null means success.
    buildStatus = buildStatus ?: 'SUCCESS'

    def color

    if (buildStatus == 'STARTED') {
        color = '#D4DADF'
    } else if (buildStatus == 'SUCCESS') {
        color = '#2cc939'
    } else if (buildStatus == 'UNSTABLE') {
        color = '#fc983a'
    } else {
        color = '#dd2e31'
    }

    notifySlack("${buildStatus}", "gitlab",
    [[
       title: "${env.BRANCH_NAME} build #${env.BUILD_NUMBER}",
       color: color,
       text: "`${env.JOB_NAME}`: " + buildStatus + "\n${env.BUILD_URL}"
    ]])
}


// Documentation step varibales
DOCUMENT_NAME = "main"
DOCUMENTATION_DIR = "./documentation/paper"
SOURCE_DIR = "./source"

node {
    try {
        slackPrepare()
        checkout scm

        Random random = new Random()
        def testRPCPort = Math.abs(random.nextInt() % 10000) + 10000
        // def couchbasePort = testRPCPort + 1
        def testRPCName = "testRPC-" + testRPCPort
        // def couchbasename = "couchbase-" + couchbasePort

        parallel documentation: {
            stage('pdflatex & biber') {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                dir (DOCUMENTATION_DIR) {
                    sh('./make.sh')
                }
            }
            stage('artifacts') {
                archiveArtifacts artifacts: "**/" + DOCUMENT_NAME + ".pdf", fingerprint: true
            }
        },
        java: {
            try {
                stage('start test container') {
                    echo "TestRPC port: " + testRPCPort

                    sshagent (credentials: ['d76de830-c6b6-4aee-b397-5d8465864f17']) {
                        //sh 'ssh -o StrictHostKeyChecking=no -l jenkins srv01.snet.tu-berlin.de ' + './jenkins-container.sh' + ' -n ' + couchbasename + ' -p ' + couchbasePort + ' -r ' + (couchbasePort + 1) + '-' + (couchbasePort + 4) + ' -d couchbase'  + ' -s start'
                        sh 'ssh -o StrictHostKeyChecking=no -l jenkins srv01.snet.tu-berlin.de ' + './jenkins-container.sh' + ' -n ' + testRPCName + ' -p ' + testRPCPort + ' -d testRPC'  + ' -s start'
                    }
                }

                stage('gradle test') {
                    
                    env.SPRING_PROFILES_ACTIVE = "test"
                    env.BLOCKCHAIN_IDENTITY_ETHEREUM_PORT = testRPCPort
                
                    echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                    sh('printenv')
                    dir (SOURCE_DIR) {
                        sh('./gradlew assemble')
                        try {
                            sh('./gradlew test')
                        } finally {
                            step([$class: 'JUnitResultArchiver', testResults: '**/test-results/test/*.xml'])
                        }
                    }
                }
            } finally {
                stage('stop test container') {
                    echo "Stopping test container..."
                    echo "TestRPC port: " + testRPCPort

                    sshagent (credentials: ['d76de830-c6b6-4aee-b397-5d8465864f17']) {
                        //sh 'ssh -o StrictHostKeyChecking=no -l jenkins srv01.snet.tu-berlin.de ' + './jenkins-container.sh' + ' -n ' + couchbasename + ' -s stop'
                        sh 'ssh -o StrictHostKeyChecking=no -l jenkins srv01.snet.tu-berlin.de ' + './jenkins-container.sh' + ' -n ' + testRPCName + ' -s stop'
                    }
                }
            }
        }, node: {
            env.NODEJS_HOME = "${tool 'node-7.8.0'}"
            
            env.PATH="${env.NODEJS_HOME}/bin:${env.PATH}"
            sh 'npm --version'

            dir (SOURCE_DIR + "/client-frontend") {
                sh "printenv"
                sh "ls ${env.NODEJS_HOME}/bin"
                sh 'npm test'
            }
        }

        stage('deploy') {
            if("${env.BRANCH_NAME}" == "dev") {
                echo "Restarting docker container 'srv01.snet.tu-berlin.de'"
                sshagent (credentials: ['d76de830-c6b6-4aee-b397-5d8465864f17']) {
                    sh 'ssh -o StrictHostKeyChecking=no -l jenkins srv01.snet.tu-berlin.de ./restart_infrastructure.sh'
                }
            }
        }
        currentBuild.result = 'SUCCESS'
    } catch (e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        slackPrepare(currentBuild.result)
    }
}

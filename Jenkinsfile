// These 2 need to be authorized using jenkins script approval
// http://your.jenkins.host/scriptApproval/
import groovy.json.JsonOutput
import java.util.Optional


// Documentation step varibales
DOCUMENT_NAME = "main"
DOCUMENTATION_DIR = "./documentation/paper"

node {
    try {
        notifySlack()

        stage('build documentation') {
            steps {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                dir (DOCUMENTATION_DIR) {
                    sh "./make"
                }
            }
        }
        post {
            always {
                archiveArtifacts artifacts: "**/" + DOCUMENT_NAME + ".pdf", fingerprint: true
            }
        }

        currentBuild.result = 'SUCCESS'
    } catch (e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        notifySlack(currentBuild.result)
    }
}
    



def notifySlack(String buildStatus = 'STARTED') {
    // Build status of null means success.
    buildStatus = buildStatus ?: 'SUCCESS'

    def color

    if (buildStatus == 'STARTED') {
        color = '#D4DADF'
    } else if (buildStatus == 'SUCCESS') {
        color = '#BDFFC3'
    } else if (buildStatus == 'UNSTABLE') {
        color = '#FFFE89'
    } else {
        color = '#FF9FA1'
    }

    slackSend("${buildStatus}", "#gitlab"
    [[
       title: "${env.BRANCH_NAME} build #${env.BUILD_NUMBER}",
       color: color,
       text: "`${env.JOB_NAME}`: " + buildStatus + "\n${env.BUILD_URL}"
    ]])
}



// Add whichever params you think you'd most want to have
// replace the slackURL below with the hook url provided by
// slack when you configure the webhook

def slackSend(text, channel, attachments) {

    //your  slack integration url
    def slackURL = ' https://hooks.slack.com/services/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' 
    //from the jenkins wiki, you can updload an avatar and
    //use that one
    def jenkinsIcon = 'https://wiki.jenkins-ci.org/download/attachments/327683/JENKINS?version=1&modificationDate=1302750804000'
    
    def payload = JsonOutput.toJson([text      : text,
                                     channel   : channel,
                                     username  : "jenkins",
                                     icon_url: jenkinsIcon,
                                     attachments: attachments])
                                     
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
}

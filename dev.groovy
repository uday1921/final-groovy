import org.ini4j.Ini

def devApprovalstage() 
{
    
    
        sendemail()      
	def instance = input( message: "Would you like to provide Approval for testing in test Environment? ", submitterParameter: 'submitter',submitter: 'udaykumar,kerlu')
	USER_INFO = instance
}
def sendemail() {
    def jobname = "${env.JOB_NAME}".split('/').first()
    def array = jobname.split("/")
    jobname = array[0];
    def branchname = "${env.JOB_NAME}".split('/').last()
    recipents = 'udaykumar.gorrepati123@gmail.com,kerlu1921@gmail.com'
    echo "${recipents}"
    try {
            emailext subject: "Need DEV Approval for Jenkins Build ${env.BUILD_NUMBER}: Job ${jobname}/${env.BRANCH_NAME}",
            to: "${recipents}",
            mimeType: 'text/html',
            body: "<b>Need DEV Approval in jenkins for migration to testing  Stage, click here :</b> ${BUILD_URL}"
    }
    catch(error) {
            echo "Error Message: ${error}"
            if (error.toString().indexOf('User Unknown') != -1) {
                    try {
                            def errmessage = ((error.toString()).replace("<","(")).replace(">",")")
                            mail bcc: '',
                            subject: "Send email from Jenkins pipeline is not successful for unlok Appllication",
                            to: "${recipents}",
                            mimeType: 'text/html',
                            body: "Send email from Jenkins pipeline is not successful for Unlok Application.<br> <br> Error Message: ${errmessage}",
                            cc: '',
                            from: 'Jenkins <noreply@motorolasolutions.com>',
                            replyTo: 'no reply'
                    } 
                    catch(err) {
                            echo "Error Message: ${err}"
                    }
            }
            else {
                    currentBuild.result = 'ABORTED'
                    error('Aborting the process')
            }
    }
}

def Cred(key,value){
    def ldapCreds = new Ini(new FileInputStream("${JENKINS_HOME}/workspace/allcreds.cnf.txt"));
    return ldapCreds.get(key, value)
}
return this
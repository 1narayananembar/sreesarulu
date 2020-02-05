
package src.com.softwareag.apigateway

    def shutdown(installDir, tenant){

        if (System.properties['os.name'].toLowerCase().contains('windows')) {
        installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
        dir(installationDir){
            bat "shutdown.bat"
        }
        }
        else {
            installationDir = "${installDir}\\profiles\\IS_" + "${tenant}" + "\\bin"
            dir(installationDir){
                sh "shutdown.sh"
            }
        }
    }


    def startup(installDir, tenant){

        if (System.properties['os.name'].toLowerCase().contains('windows')) {
            installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
            dir(installationDir) {
                bat "startup.bat"
            }
        }
        else {
            installationDir = "${installDir}\\profiles\\IS_" + "${tenant}" + "\\bin"
            dir(installationDir){
                sh "startup.sh"
            }
        }
   }


def verifyAPIGatewayIsRunning() {
    print("Try to check APIGateway is up and running")
    retryAttempts = 10
    attempt = 0
    while(attempt<retryAttempts) {
        if(pingService()==200) {
            print("API Gateway is up and running")
            return  true
        }
        Thread.sleep("10000")
        attempt++
        print("sleeping for 10 seconds");
        print("Total sleep time "+attempt*10)

    }
    print("APIGateway is not getting started")
    return false
}

def pingService() {
    //assuming the scripts run in the same environment as apigateway
    def get = new URL("http://localhost:5555/rest/apigateway/health").openConnection();
    get.setRequestProperty("Content-Type", "application/json");
    get.setRequestProperty("Accept", "application/json")
    return get.getResponseCode();
}

static  void main(String[] args) {
    verifyAPIGatewayIsRunning()
}

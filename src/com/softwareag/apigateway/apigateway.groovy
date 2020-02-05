
package src.com.softwareag.apigateway

    def shutdown(installDir, tenant){

        if (System.properties['os.name'].toLowerCase().contains('windows')) {
        installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
        dir(installationDir){
            bat "shutdown.bat"
        }
        }
        else {
            installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
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
            installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
            dir(installationDir){
                sh "startup.sh"
            }
        }
   }


def verifyAPIGatewayIsRunning() {
    retryAttempts = 15
    attempt = 0
    println("checking whether APIGateway has started ")
    while(attempt<retryAttempts) {
        try {
        if(pingService()==200) {
            print("API Gateway is up and running")
            return  true
        }
        }
        catch (Exception e) {
            println("Attempting in another 10 seconds");
        }
        Thread.sleep(10000)
        attempt++
        println("Total sleep time "+attempt*10+" sec")

    }
    print("APIGateway is not getting started")
    return false
}

def pingService() {
        //assuming the scripts run in the same environment as APIGateway and use the default port
        def get = new URL("http://localhost:5555/rest/apigateway/health").openConnection();
        get.setRequestProperty("Content-Type", "application/json");
        get.setRequestProperty("Accept", "application/json")
        return get.getResponseCode();
}

static  void main(String[] args) {
    shutdown("c:/apigateway105","default");
    startup("c:/apigateway105","default");
    verifyAPIGatewayIsRunning()
}

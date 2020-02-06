
package src.com.softwareag.apigateway

    def shutdown(installDir, tenant){
        installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"

        if (System.properties['os.name'].toLowerCase().contains('windows')) {
        dir(installationDir){
            bat "shutdown.bat"
        }
        }
        else {
            dir(installationDir){
                sh "shutdown.sh"
            }
        }
    }


    def startup(installDir, tenant){
        installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
        if (System.properties['os.name'].toLowerCase().contains('windows')) {
            dir(installationDir) {
                bat "startup.bat"
            }
        }
        else {
            dir(installationDir){
                sh "startup.sh"
            }
        }
   }


def verifyAPIGatewayIsRunning() {
    retryAttempts = 20
    attempt = 0
    println("checking whether APIGateway has started ")
    while(attempt<retryAttempts) {
        try {
        if(pingService()==200) {
            print("API Gateway is up and running")
            return
        }
        }
        catch (Exception e) {
            println("Attempting in another 10 seconds");
        }
        Thread.sleep(10000)
        attempt++
        println("Total sleep time "+attempt*10+" sec")

    }
    error("APIGateway is not getting started")
}

def pingService() {
        //assuming the scripts run in the same environment as APIGateway and use the default port
        def get = new URL("http://localhost:5555/rest/apigateway/health").openConnection();
        get.setRequestProperty("Content-Type", "application/json");
        get.setRequestProperty("Accept", "application/json")
        return get.getResponseCode();
}

static  void main(String[] args) {
    verifyAPIGatewayIsRunning()
}


package src.com.softwareag.apigateway
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

    def shutdown(installDir, tenant){
        installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
        dir(installationDir) {
            if (System.properties['os.name'].toLowerCase().contains('windows')) {

                bat "shutdown.bat"

            } else {
                sh "shutdown.sh"
            }
        }
    }


    def startup(installDir, tenant){
        installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
        dir(installationDir) {
            if (System.properties['os.name'].toLowerCase().contains('windows')) {

                bat "startup.bat"

            } else {
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
    //error("APIGateway is not getting started")
}

def pingService() {
        //assuming the scripts run in the same environment as APIGateway and use the default port
        def get = new URL("http://localhost:5555/rest/apigateway/health").openConnection();
        get.setRequestProperty("Content-Type", "application/json");
        get.setRequestProperty("Accept", "application/json")
        return get.getResponseCode();
}

def installAPIGateway(jarLocation,installDir, readscript) {

    dir(jarLocation) {
        if (System.properties['os.name'].toLowerCase().contains('windows')) {
            bat "java -jar SoftwareAGInstaller.jar -installDir " + $ {installDir} + " -readScript " + "${readscript}"
        } else {
            sh "java - jar SoftwareAGInstaller.jar -installDir " + $ {installDir} + " -readScript " + "${readscript}"
        }
    }
}

def unInstallAPIGateway(installationDir){


    dir(installationDir) {
        if (System.properties['os.name'].toLowerCase().contains('windows')) {
            bat "java -jar ${installationDir}/install/jars/Distman.jar -installDir " + installationDir + " -readUninstallScript -console"
        }
        else {
            sh "java -jar ${installationDir}/install/jars/Distman.jar -installDir " + installationDir + " -readUninstallScript -console"
        }
    }
}

//download jar
def downloadInstallationJar(String fileURL, String saveDir)
{
    try {
        int BUFFER_SIZE = 4096;
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            //error("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
    catch (Exception e)
    {
        print("Error in downloading jar file "+e)
        //error("Issue in downloading the Installer jar " + e)
    }
}



//ping all native services
def pingAllNativeServices() {

}

//replace silent script parameters
def replaceSilentScript(location,params) {
    //Assuming the name of the script file doesnt change
    def originalScript = new File(location+'InstallGateway.txt')
    for(parameter in params.keySet()) {
        def temp  = originalScript.text.replace(parameter,params.get(parameter))
        originalScript.text = temp
    }
}

def deleteInstallationfolder(location) {
   dir(location) {
       deleteDir()
   }
}

static  void main(String[] args) {
    //script execution samples

    //can we use the same installer jar for all version ?
    //def params  = ['${InstallationLocation}':'c:/apigateway107','${Serverurl}':'http://aquarius_dae.eur.ad.sag/cgi-bin/dataserveYAI_PI_107oct2020.cgi','${microgatewaylicense}':'Microgateway103.xml','${apigatewaylicense}':'49_APIGatewayAdvanced101.xml','${licenselocation}':'C:/Users/srag/Desktop/license']
    //replaceSilentScript(params)
    //downloadInstallationJar("http://aquarius_dae.eur.ad.sag/PDShare/WWW/dataserve107oct2020_SIC/data/SoftwareAGInstaller.jar","C:/Users/srag/Downloads")

    //unInstallAPIGateway('C:\\apigateway107')

    deleteInstallationfolder('c:/apigateweay107')
}

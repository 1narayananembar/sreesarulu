#!/usr/bin/groovy

package src.com.softwareag.apigateway

def shutdown(installDir, tenant){
    //check based on platform.
    installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
    dir(installationDir){
        bat "shutdown.bat"
    }

    //Make sure LOCKFILE is deleted.
    //delete elasticsearch.pid also
}

def startup(installDir, tenant){
    //check based on platform.
    installationDir = "${installDir}/profiles/IS_" + "${tenant}" + "/bin"
    dir(installationDir){
        bat "startup.bat"
    }

    //Make sure LOCKFILE is deleted.
    //delete elasticsearch.pid also

}
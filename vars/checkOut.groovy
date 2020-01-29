#!/usr/bin/env groovy



def call(Map checkOutDetails) {
  node ("${checkOutDetails.node}"){
    stage("Check out data from " + "${checkOutDetails.repo}"){
        dir("${checkOutDetails.directory}"){
        checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'dbuser', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: "${checkOutDetails.repo}"]], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
        }
	}
}
}

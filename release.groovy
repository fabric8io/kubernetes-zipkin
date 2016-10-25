#!/usr/bin/groovy
def updateDependencies(source){

  def properties = []
  // the FIS releases breaks this logic
  // properties << ['<fabric8.version>','io/fabric8/kubernetes-generator']
  // properties << ['<fabric8.maven.plugin.version>','io/fabric8/fabric8-maven-plugin']

  updatePropertyVersion{
    updates = properties
    repository = source
    project = 'fabric8io/kubernetes-zipkin'
  }
}

def stage(){
  return stageProject{
    project = 'fabric8io/kubernetes-zipkin'
    useGitTagForNextVersion = true
  }
}

def approveRelease(project){
  def releaseVersion = project[1]
  approve{
    room = null
    version = releaseVersion
    console = null
    environment = 'fabric8'
  }
}

def release(project){
  releaseProject{
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8.zipkin'
    githubOrganisation = 'fabric8io'
    artifactIdToWatchInCentral = 'zipkin-starter'
    artifactExtensionToWatchInCentral = 'jar'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    imagesToPromoteToDockerHub = []
    extraImagesToTag = null
  }
}

def updateDownstreamDependencies(stagedProject) {
  pushPomPropertyChangePR {
    propertyName = 'kubernetes.zipkin.version'
    projects = [
            'fabric8io/fabric8-platform',
            'fabric8io/fabric8-maven-dependencies',
            'fabric8io/ipaas-platform'
    ]
    version = stagedProject[1]
  }
}

def mergePullRequest(prId){
  mergeAndWaitForPullRequest{
    project = 'fabric8io/kubernetes-zipkin'
    pullRequestId = prId
  }

}
return this;

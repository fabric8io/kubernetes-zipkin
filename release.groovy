#!/usr/bin/groovy
def updateDependencies(source){

  def properties = []
  properties << ['<fabric8-maven-plugin.version>','io/fabric8/fabric8-maven-plugin']
  properties << ['<docker-maven-plugin.version>','io/fabric8/docker-maven-plugin']

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
    imagesToPromoteToDockerHub = ['zipkin-mysql']
    extraImagesToTag = null
  }
}

def mergePullRequest(prId){
  mergeAndWaitForPullRequest{
    project = 'fabric8io/kubernetes-zipkin'
    pullRequestId = prId
  }

}
return this;

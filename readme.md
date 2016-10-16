Kubernetes ZipKin
-----------------

The project provides all the required resources to start the required [ZipKin](http://zipkin.io/) components in [Kubernetes](http://kubernetes.io/) and [Openshift](https://www.openshift.com) to trace your microservices.

It has been also tested against [Minikube](https://github.com/kubernetes/minikube) and [Minishift](https://github.com/jimmidyson/minishift).

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.fabric8.zipkin/zipkin-starter-minimal/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/io.fabric8.zipkin/zipkin-starter-minimal/) ![Apache 2](http://img.shields.io/badge/license-Apache%202-red.svg)

### Getting Started

To install ZipKin in kubernetes you need to create the replication controllers and services that corresponds to the ZipKin components and their requirements.

There are 2 ways of doing that:

-   Direct installation
-   Generating and installing via Maven

### Direct installation

To directly install everything you need:

    kubectl create -f http://repo1.maven.org/maven2/io/fabric8/zipkin/zipkin-starter/0.1.5/zipkin-starter-0.1.5-kubernetes.yml
    
or if you are using openshift:
    
    oc create -f http://repo1.maven.org/maven2/io/fabric8/zipkin/zipkin-starter/0.1.5/zipkin-starter-0.1.5-openshift.yml

To directly install a minimal ZipKin *(just storage and query)*:
                        
    kubectl create -f http://repo1.maven.org/maven2/io/fabric8/zipkin/zipkin-starter-minimal/0.1.5/zipkin-starter-minimal-0.1.5-kubernetes.yml
    
or if you are using openshift:
    
    oc create -f http://repo1.maven.org/maven2/io/fabric8/zipkin/zipkin-starter-minimal/0.1.4/zipkin-starter-minimal-0.1.4-openshift.yml

Both of the above are released in json format too.

Zipkin uses a storage backend (it can be mysql, cassandra or elastic search, but currently this project only supports mysql). The storage requires a persistence volume.
So the next step is to create it. So you either need to create a persistence volume named `mysql-data`.

Or if you are using [gofabric8](https://github.com/fabric8io/gofabric8), it can automatically create it for you:
 
    gofrabric8 volumes 


### Generating the configuration

The configuration that was downloaded from the internet in the previous step can also be generated using the starter modules.
The starter module are plain maven projects that generate and apply kubernetes/openshift configuration using the [Fabric8 Maven Plugin](http://fabric8.io/guide/mavenPlugin.html)

Currently the available starters are:

-   starter-mysql
-   starter-minimal

#### MySQL Starter

The mysql starter will generate and install Zipkin using MySQL as a storage module.

    mvn clean install -Pdocker
    mvn fabric8:apply -pl starter-mysql

The first command will build the whole project and the second will apply the resources to kubernetes (including its dependencies).
So after using the above in a clean workspace, your workspace should look like this:


    kubectl get pods

NAME | READY|STATUS|RESTARTS|AGE
-----|------|------|--------|---
kafka-qw13r|1/1|Running|0|8m
zipkin-mysql-ygm1g|1/1|Running|0|8m
zipkin-roxae|1/1|Running|0|8m
zookeeper-1-my183|1/1|Running|0|8m
zookeeper-2-jx8eq|1/1|Running|0|8m
zookeeper-3-d1icz|1/1|Running|0|8m

#### Minimal Starter

The minimal starter will generate and install a Zipkin without a collector and using MySQL as a storage module. This can be also "considered" a dev mode.

    mvn clean install
    mvn fabric8:apply -pl starter-minimal

This time kafka and zookeeper will not be installed at all.

NAME | READY|STATUS|RESTARTS|AGE
-----|------|------|--------|---
zipkin-mysql-d4msa|1/1|Running|0|8m
zipkin-rpxfw|1/1|Running|0|8m

### Using the console

Once the zipkin pod is ready, you will be able to access the console using the zipkin service.

For example: http://zipkin-default.vagrant.f8 (here is how the external URL looks on openshift if zipkin service is available in the default namespace and the domain is vagrant.f8).

![ZipKin Console](images/zipkin-console.png "Zipkin Console")

### Prometheus integration

As of 0.1.5 kubernetes-zipkin comes with prometheus annotations, so that the the zipkin server is monitored by prometheus.
You can easily try out this feature by installing prometheus:

    kubectl create -f http://repo1.maven.org/maven2/io/fabric8/devops/apps/prometheus/2.2.259/prometheus-2.2.259-kubernetes.yml
    
or on openshift:

    oc create -f http://repo1.maven.org/maven2/io/fabric8/devops/apps/prometheus/2.2.259/prometheus-2.2.259-openshift.yml
   
### Running the integration tests

Some really basic integration tests have been added. The purpose of those tests is to check that configuration and images are working.
The integration tests are based on [Fabric8 Arquillian](http://fabric8.io/guide/testing.html) and require an exsting Kubernetes/Openshift environment.
So they are disabled by default. To enabled and run them:

    mvn clean install -Dk8s.skip.test=false

Kubernetes ZipKin
-----------------

The project generates all the required resources to start the required ZipKin components in Kubernetes

### Getting Started

The quicker way to get started is to just build the project and use the apply one of "starter" configurations.
Currently the available starters are:

-   starter-mysql
-   starter-minimal

#### MySQL Starter

The mysql starter will generate and install Zipkin using MySQL as a storage module.

    mvn clean install
    mvn fabric8:apply -pl starter-mysql

The first command will build the whole project and the second will apply the resources to kubernetes (including its dependencies).
So after using the above in a clean workspace, your workspace should look like this:   

    
    kubectl get pods
    
NAME | READY|STATUS|RESTARTS|AGE
-----|------|------|--------|---
kafka-qw13r|1/1|Running|0|8m
zipkin-collector-5nopi|1/1|Running|0|8m
zipkin-mysql-ygm1g|1/1|Running|0|8m
zipkin-query-roxae|1/1|Running|0|8m
zookeeper-1-my183|1/1|Running|0|8m
zookeeper-2-jx8eq|1/1|Running|0|8m
zookeeper-3-d1icz|1/1|Running|0|8m    

#### Minimal Starter

The minimal starter will generate and install a Zipkin without a collector and using MySQL as a storage module. This can be also "considered" a dev mode.

    mvn clean install
    mvn fabric8:apply -pl starter-minimal
        
This time the zipkin-collector, kafka and zookeeper will not be installed at all.    
    
NAME | READY|STATUS|RESTARTS|AGE
-----|------|------|--------|---
zipkin-mysql-d4msa|1/1|Running|0|8m
zipkin-query-rpxfw|1/1|Running|0|8m

### Using the console

Once the zipkin-query pod is ready, you will be able to access the console using the zipkin-query service.

For example: http://zipkin-query-default.vagrant.f8 (here is how the external URL looks on openshift if zipkin-query service is available in the default namespace and the domain is vagrant.f8).

![ZipKin Console](images/zipkin-console.png "Zipkin Console")
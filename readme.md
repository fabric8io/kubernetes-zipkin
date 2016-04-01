Kubernetes ZipKin
-----------------

The project generates all the required resources to start the required ZipKin components in Kubernetes

### Getting Started

The quicker way to get started is to just build the project and use the apply "starter" configuration

    mvn clean install
    mvn fabric8:apply -pl starter

The first command will build the whole project and the second will apply the resources to kubernetes (including its dependencies).
So after using the above in a clean workspace, your workspace should look like this:
    

    
    kubectl get pods
    
NAME | READY|STATUS|RESTARTS|AGE
-----|------|------|--------|---
kafka-qw13r|1/1|Running|0|8m
zipkin-collector-5nopi|1/1|Running|0|8m
zipkin-mysql-ygm1g|1/1|Running|0|8m
zipkin-query-roxae|1/1|Running|0|8m
zipkin-web-4dkm6|1/1|Running|0|8m
zookeeper-1-my183|1/1|Running|0|8m
zookeeper-2-jx8eq|1/1|Running|0|8m
zookeeper-3-d1icz|1/1|Running|0|8m    
    
    
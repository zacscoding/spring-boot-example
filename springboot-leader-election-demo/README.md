# Leader election with redis, zookeeper, raft  
; this is a demo project for leader election between clusters (i.e spring boot server)  
by using redis, zookeeper, raft  

> ## Getting started

> ### Running with zookeeper  

```aidl
// start server1
$ ./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8080 -Dcluster.id=server1 \
    -Dcluster.leadership.type=zookeeper \
    -Dcluster.leadership.zookeeper.connectString=192.168.79.130:2181"

$ ./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8081 -Dcluster.id=server2 \
    -Dcluster.leadership.type=zookeeper \
    -Dcluster.leadership.zookeeper.connectString=192.168.79.130:2181"
```  



> ### Running with redis  

```aidl
// start server1
$ ./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8080 -Dcluster.id=server1 \
    -Dcluster.leadership.type=redis \
    -Dcluster.leadership.redis.address=redis://192.168.79.130:6379"

$ _./gradlew bootRun \
    -PjvmArgs="-Dserver.port=8081 -Dcluster.id=server2 \
    -Dcluster.leadership.type=redis \
    -Dcluster.leadership.redis.address=redis://192.168.79.130:6379"_
```  



> ### Result

If u run two server, then server1 take a leader ship  

```aidl
... demo.config.LeaderElectionConfiguration  : ## onTaskLeadership from server1
```  

After terminate server1, then server2 will take a leadership  

```aidl
... demo.config.LeaderElectionConfiguration  : ## onTaskLeadership from server2
```
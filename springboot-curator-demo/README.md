# Curator demo  


> ### Basic usage  

<a href="src/test/java/demo/curator/CuratorBasicUsageTest.java">See</a>

```
usage_retryPolicies()     / usage_retryPoliciesAsync()
usage_configManagement()  / usage_watchers()
usage_typedModel()        / usage_leaderElection()
usage_sharedLock()        / usage_counter()  
```

---  

> ### Shared lock demo  

- <a href="src/main/java/demo/lock">Server side (receive tasks)</a>  
- <a href="src/test/java/demo/LockTaskPushTest.java">Client side (push tasks) </a>  

---  

> ### Master slave demo  

- <a href="src/main/java/demo/master/LeaderListener.java">LeaderListener</a>
- <a href="src/main/java/demo/master/MasterSlaveTaskController.java">Leader Selector start & check</a>

**Running 3 applications**  

- Client001  

```aidl
$ java -Dlock.client.id=0001 -Dserver.port=8081 -Dspring.profiles.active=master-slave -jar demo.jar
```

- Client002  

```aidl
$ java -Dlock.client.id=0002 -Dserver.port=8082 -Dspring.profiles.active=master-slave -jar demo.jar
```

- Client003  

```aidl
$ java -Dlock.client.id=0003 -Dserver.port=8083 -Dspring.profiles.active=master-slave -jar demo.jar
```   

=> If terminate a client having leadership, then another client will have leadership  

```
2018-12-15 01:18:28.578  INFO 5275 --- [eaderSelector-0] demo.master.LeaderListener               : [0001] task leadership.... client : org.apache.curator.framework.imps.CuratorFrameworkImpl@79191bc4
2018-12-15 01:18:29.255  INFO 5275 --- [       Thread-2] [MASTER-SLAVE]                           : [Check leader - 0001] is leader : true
```  

---

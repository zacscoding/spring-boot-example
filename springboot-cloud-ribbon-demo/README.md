# Client loadbalancer with spring cloud ribbon
; this is a demo project for client side loadbalancer with spring cloud ribbon from <a href="https://spring.io/guides/gs/client-side-load-balancing">Spring getting started</a>  

## Getting started  

> Start server1,2,3  

```aidl
// start server1
$ ./gradlew bootRun -PjvmArgs="-Dserver.port=3000 -Dspring.profiles.active=server"

// start server2
$ ./gradlew bootRun -PjvmArgs="-Dserver.port=3001 -Dspring.profiles.active=server"

// start server3
$ ./gradlew bootRun -PjvmArgs="-Dserver.port=3002 -Dspring.profiles.active=server"
```  

> Start client  

```aidl
$ ./gradlew bootRun -PjvmArgs="-Dserver.port=8080 -Dspring.profiles.active=client"
```  

> Request to client repeatedly  

```aidl
$ curl -XGET http://127.0.0.1:8080/hello
$ curl -XGET http://127.0.0.1:8080/hello
$ curl -XGET http://127.0.0.1:8080/hello
$ curl -XGET http://127.0.0.1:8080/hello
$ curl -XGET http://127.0.0.1:8080/hello
$ curl -XGET http://127.0.0.1:8080/hello
```  


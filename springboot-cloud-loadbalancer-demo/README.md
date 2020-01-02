# Client loadbalancer with spring cloud  
; this is a demo project for client side loadbalancer with spring cloud  
from <a href="https://spring.io/guides/gs/spring-cloud-loadbalancer/">Spring getting started</a>

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

> Check server1,2,3  

```aidl
$ curl -XGET http://localhost:3000/greeting

$ curl -XGET http://localhost:3001/greeting

$ curl -XGET http://localhost:3002/greeting
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
```  

Then u can see server's log that received distributed requests.  

> Request with 6 times  

server1 -> server2 -> server3 -> server1 -> server2 -> server3

```log
// server1's log
2020-01-03 00:12:56.837  INFO 12900 --- [nio-3000-exec-1] demo.server.ServerController             : Access /greeting
2020-01-03 00:13:00.876  INFO 12900 --- [nio-3000-exec-2] demo.server.ServerController             : Access /greeting

// server2's log
2020-01-03 00:12:58.852  INFO 10304 --- [nio-3001-exec-1] demo.server.ServerController             : Access /greeting
2020-01-03 00:13:01.420  INFO 10304 --- [nio-3001-exec-3] demo.server.ServerController             : Access /greeting

// server3's log
2020-01-03 00:12:59.640  INFO 16432 --- [nio-3002-exec-1] demo.server.ServerController             : Access /greeting
2020-01-03 00:13:01.957  INFO 16432 --- [nio-3002-exec-2] demo.server.ServerController             : Access /greeting
```  


> ## requirements  

There are 2 services `UpperService`,`LowerService` and divided by `groupId`.  

So we can call to client like below `GET /{groupId}/{serviceType}/{message}`.  

- UpperService `/uppercase/message`
    - group1
        - service1, service2, ... servicen
    - group2
        - service1, service2, ... servicem

- LowerService `/lowwercase/message`
    - group1
        - service1, service2, ... servicel

``
---  

> ## Examples

- UpperService (initial)
    - group1
        - service1(3011), service2(3012)
    - group2
        - service1(3021), service2(3022)  
- LowerService (Added after started)
    - group1
        - service1(4011)  
        

> ### Start upper case service group1, group2 
        

```aidl
// start upper case service group1 -> service1, service2
$ java -jar -Dserver.port=3011 -Dspring.profiles.active=upper-service build/libs/demo.jar
$ java -jar -Dserver.port=3012 -Dspring.profiles.active=upper-service build/libs/demo.jar

// start upper case service  group2 -> service1, service2
$ java -jar -Dserver.port=3021 -Dspring.profiles.active=upper-service build/libs/demo.jar
$ java -jar -Dserver.port=3022 -Dspring.profiles.active=upper-service build/libs/demo.jar
```  

```aidl
// start upper lower service  group1 -> service1, service2
$ java -jar -Dserver.port=4011 -Dspring.profiles.active=lower-service build/libs/demo.jar
$ java -jar -Dserver.port=4012 -Dspring.profiles.active=lower-service build/libs/demo.jar
```

> ### Start a client  

```aidl
$ java -jar -Dserver.port=8080 -Dspring.profiles.active=client build/libs/demo.jar
```  

> ### Request upper case to ["group1", "group2"]

```aidl
// request to "group1"
$ curl -XGET http://localhost:8080/group1/upper/HeLlO
$ curl -XGET http://localhost:8080/group1/upper/HeLlO

// request to "group2"
$ curl -XGET http://localhost:8080/group2/upper/Hi
$ curl -XGET http://localhost:8080/group2/upper/Hi
```  

> ### Start lower service group1  

```aidl
// start lower case service group1 -> service1, service2
$ java -jar -Dserver.port=4011 -Dspring.profiles.active=lower-service build/libs/demo.jar
$ java -jar -Dserver.port=4012 -Dspring.profiles.active=lower-service build/libs/demo.jar
```  

> ### Register service1,2 to `Client`  

```aidl
curl -XPOST 'http://localhost:8080/service' \
--header 'Content-Type: application/json' \
--data-raw '{
	"serviceType" : "LOWER_CASE_SERVICE",
	"groupName" : "group1",
	"schema" : "http",
	"host" : "127.0.0.1",
	"port" : 4011
}'

// Response
ServiceEntity(id=6, serviceType=LOWER_CASE_SERVICE, groupName=group1, schema=http, host=127.0.0.1, port=4011)

// Wait for checking health status
// Request lower case 
curl -XGET http://localhost:8080/group1/lower/Hi
```  

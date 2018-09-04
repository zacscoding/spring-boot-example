## Kafka demo  

## Getting start  

> server config  

```
broker.id=0
listeners=PLAINTEXT://192.168.116.128:9092
log.dirs=/tmp/kafka-logs
```

> Create topic  

```
[app@localhost kafka_2.12-2.0.0]$ ./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor=1 --partitions 1 --topic test
Created topic "test".
```  


> Check topic list  

```
[app@localhost kafka_2.12-2.0.0]$ ./bin/kafka-topics.sh --list --zookeeper localhost:2181
__consumer_offsets
test
```  

> Start a consumer  

```
[app@localhost kafka_2.12-2.0.0]$ ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test
```  

> Send some message  

```
[app@localhost kafka_2.12-2.0.0]$ ./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
>this is a message
>this is another message
```  


## Setting up a multi-broker cluster  

> Generate config file  


```
[app@localhost kafka_2.12-2.0.0]$ cp config/server.properties config/server-1.propperties
[app@localhost kafka_2.12-2.0.0]$ cp config/server.properties config/server-2.propperties
```  

> Change properties  

```
[app@localhost kafka_2.12-2.0.0]$ vi config/server-1.propperties 
broker.id=1
listeners=PLAINTEXT://:9093
log.dirs=/tmp/kafka-logs-1
```

```
[app@localhost kafka_2.12-2.0.0]$ vi config/server-2.propperties 
broker.id=2
listeners=PLAINTEXT://:9094
log.dirs=/tmp/kafka-logs-2
```


---  

## Spring + Kafka demos  

### Basic  
; Produce, Consume  

[[source code]](https://github.com/zacscoding/spring-boot-example/tree/master/springboot-kafka-demo/src/main/java/demo/basic)  

> application.properties  

```
spring.profiles.active=basic
basic.kafka.bootstrap-servers=192.168.116.128:9092
```



### Rpc request - response (ReplyingKafkaTemplate<K, V, R>)  
; Generate request and reply response  

[[source code]](https://github.com/zacscoding/spring-boot-example/tree/master/springboot-kafka-demo/src/main/java/demo/rpc)  
  
![kafka-rpc-request-response](https://user-images.githubusercontent.com/25560203/45033927-0aa2fb80-b091-11e8-945e-1514216ecadd.png)

> application.properties  

```
spring.profiles.active=rpc

## for rpc
# https://dzone.com/articles/synchronous-kafka-using-spring-request-reply-1
rpc.kafka.bootstrap-servers=192.168.116.128:9092
rpc.kafka.topic.request-topic=request-topic
rpc.kafka.topic.requestreply-topic=requestreply-topic
rpc.kafka.consumergroup=requestreplygorup
```  


- href : https://dzone.com/articles/synchronous-kafka-using-spring-request-reply-1  

---  
  


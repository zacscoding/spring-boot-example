## Install

#### Centos7  

http://sangchul.kr/647  

> EPEL 저장소 설치  

```
yum install -y epel-release
```  

> erlang 설치  

```
yum install -y erlang
```  

> RabbitMQ 설치  

```
yum install -y rabbitmq-server
```  

> Web Console 활성화  

```
rabbitmq-plugins enable rabbitmq_management  
```  

> RabbitMQ 서비스 등록  

```
rabbitmq-plugins enable rabbitmq_management  
(rabbitmq-server.service disabled

systemctl enable rabbitmq-server
```

> RabbitMQ 서비스 시작  

```
systemctl start rabbitmq-server
```  


## RabbitMQ CLI

> 유저 확인  

```
[root@localhost rabbitmq]# rabbitmqctl list_users
Listing users ...
guest	[administrator]
...done.
```  

> 사용자 추가  

```
[root@localhost rabbitmq]# rabbitmqctl add_user test test
Creating user "test" ...
...done.
[root@localhost rabbitmq]# rabbitmqctl list_users
Listing users ...
guest	[administrator]
test	[]
...done.
```  

> 사용자에게 태그설정  

```
[root@localhost rabbitmq]# rabbitmqctl set_user_tags test administrator
Setting tags for user "test" to [administrator] ...
...done.
[root@localhost rabbitmq]# rabbitmqctl list_users;
Listing users ...
guest	[administrator]
test	[administrator]
...done.
```  

> 사용자 접속 퍼미션 설정  

```
[root@localhost rabbitmq]# rabbitmqctl set_permissions -p / test ".*" ".*" ".*"
Setting permissions for user "test" in vhost "/" ...
...done.
[root@localhost rabbitmq]# rabbitmqctl list_permissions
Listing permissions in vhost "/" ...
guest	.*	.*	.*
test	.*	.*	.*
...done.
```  

> 사용자 제거  

```
[root@localhost rabbitmq]# rabbitmqctl delete_user test2
Deleting user "test2" ...
...done.
```  

> 사용자 비밀번호 변경  

```
# rabbitmqctl change_password <사용자> <신규비번>
```  

> RabbitMQ WEB  

```
http://localhost:15672
```


---  

## refs  

- https://www.rabbitmq.com/tutorials/tutorial-one-java.html
- https://spring.io/guides/gs/messaging-rabbitmq/  





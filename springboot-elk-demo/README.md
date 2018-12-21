## Spring app + ELK  

### Server1  

- server
- filebeat  

### Server2  

- logstash
- elasticsearch
- kibana  


---  

### Elasticsearch 설치  

> tar 압축 해제  

```aidl
app@app:~/elasticsearch$ tar -xvf elasticsearch-6.3.0.tar.gz 
elasticsearch-6.3.0/
```  

> config  

```aidl
...
cluster.name: my-application
...
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 0.0.0.0 
#
# Set a custom port for HTTP:

http.port: 9200
http.enabled: true
```  

> settings  

```aidl
$vi /etc/security/limit.conf  

https://www.elastic.co/guide/en/elasticsearch/reference/current/file-descriptors.html  

#<domain>      <type>  <item>         <value>
app             -        nofile         65536
```  

```aidl
$ vi /etc/sysctl.conf

vm.max_map_count=262144
```  






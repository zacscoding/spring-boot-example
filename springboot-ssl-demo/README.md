## Demo ssl with spring boot

### HTTPS Spring boot server  

> Key gen to springboot-ssl-demo/keystore.p12  

ref : https://jojoldu.tistory.com/350

```aidl
keytool -genkey -alias bns-ssl -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650 
```   

> application.yaml

```aidl
server:
  ssl:
    enabled: true
    key-store: keystore.p12
    key-store-password: passwd
    key-store-type: PKCS12
    key-alias: bns-ssl
  port: 8443
```  

> request  

```aidl
zaccoding@zaccoding:~$ curl --insecure https://localhost:8443
Welcome :)
```  

--- 

### HTTP client  

- TODO...

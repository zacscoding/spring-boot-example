# Spring boot with dynamodb  
; this is a demo project for learning dynamodb with spring boot  

## Commands   

> ## Start dynamodb with docker  

```cmd
$ cd docker && docker-compose up -d
```  

> ## Run aws cli  

Start dynamodb with docker and tests Create table / Create item / Get item / Update item / Delete item / Drop table.  

```cmd
$ scripts/aws-cli.sh
```  

> ## Run with aws-java-sdk-dynamodb and testcontainers  

Start dynamodb with testcontainers and tests Create table / Create item / Get item / Update item / Delete item / Drop table.

[See SdkBasicUsage.java](./src/test/java/demo/basic/SdkBasicUsage.java)  
[See MapperBasicUsage.java](./src/test/java/demo/basic/MapperBasicUsage.java)    

> ## Simple CRUD api with spring data dynamodb  

[See config package](./src/main/java/demo/config)  
[See rest controller](./src/main/java/demo/rest/CommentController.java)  

---  

## References  

- https://woowabros.github.io/study/2019/06/05/spring-data-dynamodb-1.html
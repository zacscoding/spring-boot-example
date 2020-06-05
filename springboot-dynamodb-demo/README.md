# Spring boot with dynamodb  
; this is a demo project for spring with dynamodb  

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

---  

## References  

- https://woowabros.github.io/study/2019/06/05/spring-data-dynamodb-1.html
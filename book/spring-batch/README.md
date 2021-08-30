# Learning Spring Batch :)  

- [Study Github](https://github.com/Meet-Coder-Study/book-spring-batch)
- [스프링 배치 완벽 가이드 2/e](https://book.naver.com/bookdb/book_detail.nhn?bid=18990242)  
  - [Source Code](https://github.com/AcornPublishing/definitive-spring-batch)  

# Running examples  

```shell
// Setup mysql with docker-compose
$  ./tools/scripts/compose.sh up

// Maven clean & install. available args: ["ch2", "ch3", ...]
$ ./tools/scripts/build.sh ch3

// Running jar
$ java -jar -Dspring.profiles.active=step-example ./Chapter03/target/Chapter03-1.0-SNAPSHOT.jar 
```

---  

# Table of contents  

- [Ch1. 배치와 스프링](./01_배치와_스프링.md)  
- [Ch2. 스프링 배치](./02_스프링_배치.md)  
- [Ch3. 예제 잡 애플리케이션](./03_예제_잡_애플리케이션.md)  
- [Ch4. 잡과 스텝 이해하기](./04_잡과_스텝_이해하기.md)









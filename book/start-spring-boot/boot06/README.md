## 06 Spring MVC를 이용한 통합

- <a href="#init"> Init </a>


<div id="init"></div>

### Init

> Project init

![initialize](./pics/[ch06-01]init.png)

> Thymeleaf layout

```
<dependency>
    <groupId>nz.net.ultraq.thymeleaf</groupId>
    <artifactId>thymeleaf-layout-dialect</artifactId>
    <version>2.2.2</version>
</dependency>
```

> application.properties

```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/jpa_ex?useSSL=false
spring.datasource.username=jpa_user
spring.datasource.password=jpa_user

spring.jpa.hibernate.ddl-auto=update
spring.jpa.generate-ddl=true
spring.jpa.show-sql=true
spring.jpa.database=mysql
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect

logging.level.org.hibernate=info

spring.thymeleaf.cache=false

logging.level.org.springframework.web=info
logging.level.org.zerock=info
```

> bootstrap cdn

> Controller, Domain , Repository 작성

> Querydsl 설정

```
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-apt</artifactId>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-jpa</artifactId>
</dependency>

...

<plugin>
    <groupId>com.mysema.maven</groupId>
    <artifactId>apt-maven-plugin</artifactId>
    <version>1.1.3</version>
    <executions>
        <execution>
			<goals>
                <goal>process</goal>
            </goals>
            <configuration>
                <outputDirectory>target/generated-sources/java</outputDirectory>
                <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
            </configuration>
        </execution>
    </executions>
</plugin>

```

> Board CRUD

- Controller
<a href="https://github.com/zacscoding/spring-boot-example/tree/master/book/start-spring-boot/boot06/src/main/java/org/zerock/controller/WebBoardController.java">WebBoardController</a>


- Persistence
<a href="https://github.com/zacscoding/spring-boot-example/tree/master/book/start-spring-boot/boot06/src/main/java/org/zerock/persistence/WebBoardRepository.java">WebBoardRepository</a>

- Domain
<a href="https://github.com/zacscoding/spring-boot-example/tree/master/book/start-spring-boot/boot06/src/main/java/org/zerock/domain/WebBoard.java">WebBoard</a>

- DTO
  + <a href="https://github.com/zacscoding/spring-boot-example/tree/master/book/start-spring-boot/boot06/src/main/java/org/zerock/vo/PageMaker.java">PageMaker</a>
  + <a href="https://github.com/zacscoding/spring-boot-example/tree/master/book/start-spring-boot/boot06/src/main/java/org/zerock/vo/PageVO.java">PageVO</a>

- Views
<a href="https://github.com/zacscoding/spring-boot-example/tree/master/book/start-spring-boot/src/main/resources/templates">templates</a>

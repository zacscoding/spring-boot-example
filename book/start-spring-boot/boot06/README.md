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


---


## ch07 REST 방식의 댓글 처리 + JPA 처리




```
Hibernate: create table tbl_webreplies (bno bigint not null auto_increment, regdate datetime, reply_text varchar(255), replyer varchar(255), updatedate datetime, board_bno bigint, primary key (bno)) engine=InnoDB
Hibernate: alter table tbl_webreplies add constraint FKoqessctbkmr17s2vyoy825r2s foreign key (board_bno) references tbl_webboards (bno)
```


#### N+1 검색 문제

> WebBoard.java

```
...
public class WebBoard {
  ...
  @OneToMany(mappedBy = "board", fetch=FetchType.LAZY)
  private List<WebReply> replies;
}

-> list query

Hibernate: select webboard0_.bno as bno1_0_, webboard0_.content as content2_0_, webboard0_.regdate as regdate3_0_, webboard0_.title as title4_0_, webboard0_.updatedate as updateda5_0_, webboard0_.writer as writer6_0_ from tbl_webboards webboard0_ where webboard0_.bno>? order by webboard0_.bno desc limit ?
Hibernate: select count(webboard0_.bno) as col_0_0_ from tbl_webboards webboard0_ where webboard0_.bno>?

Hibernate: select replies0_.board_bno as board_bn6_1_0_, replies0_.rno as rno1_1_0_, replies0_.rno as rno1_1_1_, replies0_.board_bno as board_bn6_1_1_, replies0_.regdate as regdate2_1_1_, replies0_.reply_text as reply_te3_1_1_, replies0_.replyer as replyer4_1_1_, replies0_.updatedate as updateda5_1_1_ from tbl_webreplies replies0_ where replies0_.board_bno=?
X10
```

##### SOl1) @Query

> WebBoardRepository.java

```
@Query("SELECT b.bno, b.title, b.writer, b.regdate, count(r) FROM WebBoard b"
      + "LEFT OUTER JOIN b.replies r WHERE b.bno > 0 GROUP BY b")
```

> Test Code

```
Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "bno");
List<Object[]> list = WebBoardRepository.getListWithQuery(pageable);
list.forEach(arr -> log.info(Arrays.toString(arr)));
```








<br /><br /><br /><br /><br /><br /><br />


---


















---

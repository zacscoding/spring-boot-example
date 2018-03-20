# ch03. 구성을 사용자화하기

- <a href="#3.1">3.1 스프링 부트 자동 구성 오버라이드하기 </a>
- <a href="#3.2">3.2 프로퍼티를 이용하여 외부적으로 구성하기 </a>
- <a href="#3.3">3.3 애플리케이션 오류 페이지 사용자 정의하기</a>
- <a href="#3.4">요약 </a>

---

<div id="3.1"></div>

## 3.1 스프링 부트 자동 구성 오버라이드하기
; 아무것도 구성하지 않고도 명시적으로 구성한 것과 동일한 결과를 얻는다면, 당연히  
구성이 없는 편을 선택  
=> 대부분 자동 구성된 빈은 원하는 것을 정확하게 제공하므로 오버라이드 할 필요가 없지만,  
자동 구성으로 원하는 기능이 잘 동작하지 않을 때도 있음 (e.g : 보안)  

### 3.1.1 애플리케이션 보안 설정하기  
; 시큐리티 스타터를 빌드에 추가  

> Gradle  

```
compile('org.springframework.boot:spring-boot-starter-security')
```  

> Maven  

```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```  

=> 기본으로 실행하면, stdout으로 암호 출력  

```
Using generated security password: 494fef69-217c-4f33-b35f-642b4e366718
```  

### 3.1.2 사용자 정의 보안 구성하기  
; Web

- SecurityConfig
- login.html
- ReadingListApplication
- ReaderHandlerMethodArgumentResolver
- ReadingListController
- Reader
- ReaderRepository

### 3.1.3 자동 구성에 숨은 기능 엿보기  


> JdbcTemplate()  

```
@Bean
@ConditinalOnMissingBean(JdbcOperations.class)
public JdbcTemplate jdbcTemplate() {
  return new JdbcTemplate(this.dataSource);
}
```  
=> @ConditinalOnMissingBean에 의해 JdbcOperations 타입의 빈이 없을 때만 작동  

> SpringSecurity  

```
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
public class SpringBootWebSecurityConfiguration {
  ...
}
```  

- @ConditionalOnClass : 클래스패스에 WebSecurityConfigurerAdapter 있는지 확인
- @ConditionalOnMissingBean : WebSecurityConfigurerAdapter대신 커스텀 클래스를  
사용가능하도록 함  
=> WebSecurityConfigurerAdapter가 없을 때에만 자동 구성  
- @ConditionalOnWebApplication : 웹 APP 인지 확인  

---  

<div id="3.2"></div>

## 3.2 프로퍼티를 이용하여 외부적으로 구성하기  
; 스프링 부트가 자동으로 구성하는 빈들은 세부적인 부분을 조정할 수 있도록 300개가 넘는  
프로퍼티 제공  

> e.g 스프링의 매너 숨기기

```
1) VM arguments
java -jar build/libs/readinglist-0.0.1-SNAPSHOT.jar --spring.main.show-banner=false

2) application.properties  
spring.main.show-banner=false

3) application.yml
spring :
  main :
    show-banner : false  
4) 환경 변수로 설정(BASH나 zsh 쉘 . or - 대신 _ 로 사용)    
export spring_main_show_banner=false
```  

> 스프링 부트 프로퍼티를 설정하는 방법(우선순위 순)  

1. 명령줄 인자  
2. java:comp/env 에서 얻을 수 있는 JNDI 속성  
3. JVM 시스템 프로퍼티  
4. 운영체제의 환경 변수  
5. random.\*로 시작하는 프로퍼티 때문에 무작위로 생성된 값(${random.long})처럼  
다른 프로퍼티를 설정할 때 참조)  
6. APP 외부의 application.properties|yaml  
7. APP 내부에 패키징 된 application.properties|yaml
8. @PropertySource로 지정된 프로퍼티 소스
9. 기본 프로퍼티  

> application.properties|yml  

1. 외부적으로 APP이 작동하는 디렉터리의 /config 하위 디렉터리
2. 외부적으로 APP이 작동하는 디렉터리  
3. 내부적으로 config 패키지  
4. 내부적으로 클래스패스의 루트  

=> /config 하위 디렉터리에 있는 application.properties 파일은 APP classpath에 있는  
것보다 우선순위가 높음 | yml이 properties보다 우선순위가 높음  

### 3.2.1 자동 구성 미세하게 조정하기  

**템플릿 캐싱 비활성화**  

- thymeleaf : ```spring.thymeleaf.cache=false```  
- freemarker : ```spring.freemarker.cache=false```  
- groovy-template : ```spring.groovy.template.cache=false```  
- velocity : ```spring.velocity.cache=false```  

**내장 서버 구성**  

> 내장 서버 포트 변경  

; java -jar build/libs/readinglist-0.0.1-SNAPSHOT.jar --server.port=8000  
== server.port=8000  

> HTTPS로 전송할 때  
1. JDK의 keytool 유틸리티로 키스토어 생성  
```$ keytool -keystore mykeys.jks -genkey -alias tomcat -keyalg RSA```  
2. 설정  
```
server :
  port : 8443
  ssl :
    key-store : file//path/to/mykeys.jks
    key-store-password : letmein
    key-password : letmein
```  

**로깅 구성**  
; 기본적으로 스프링부트는 INFO 레벨의 로그를 표시하려고 로그백으로 로깅을 설정  

> 기본적인 logback.xml  

```
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>
        %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} -%msg%n
      </pattern>
    </encoder>
  </appender>

  <logger name="root" level="INFO" />
  <root level="INFO">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```  

=> 로깅 레벨을 설정하려면 logging.level 다음에 로깅 레벨을 설정하는 로거 이름을  
붙인 프로퍼티를 생성하면 됨  

> e.g) path : /var/logs/ , file : BookWorm.log &  
루트 로깅 레벨은 WARN & 시큐리티는 DEBUG

```
1) application.yml  

logging:
  path: /var/logs/
  file: BookWorm.log
  level:
    root: WARN
    org:
      springframework:
      security: DEBUG
    (or == org.springframework.security: DEBUG)

2) application.properties  
logging.path=/var/logs/
logging.file=BookWorm.log
logging.level.root=WARN
logging.level.org.springframework.security=DEBUG
```   

> logback -> log4j 설정  

1. 기본 로깅 스타터를 제외  
```
<!-- maven -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter</artifactId>
  <exclusions>
    <exclusion>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-logging</artifactId>
    </exclusion>
  </exclusions>
</dependency>  


//gradle  
configurations {
  all*.exclude group:'org.springframework.boot', module:'spring-boot-starter-logging'
}
```  

2. log4j 추가  

```
<!-- maven -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-log4j</artifactId>  
</dependency>  

//gradle  
compile('org.springframework.boot:spring-boot-starter-log4j')
```

**데이터 소스 구성**  

> e.g : MySQL DB 사용  

```
spring:
  datasource:
    url: jdbc:mysql://localhost/readingList  
    username: dbuser
    password: dbpass
    driver-class-name: com.mysql.jdbc.Driver  

// JDBC 드라이버는 명시하지 않아도 되지만(URL 보고 판단)    
// 문제가 발생하면 프로퍼티를 설정
```  

=> 톰캣의 풀링 DataSource가 클래스패스에 있다면 DataSource 빈은 풀링  
그렇지 않으면 클래스패스에서 다른 커넥션 풀 구현체를 찾아 사용  
- HikariCP
- Commons DBCP  
- Commons DBCP 2

> JNDI에서 DataSource 찾기  

```
spring:
  datasource:
    jndi-name: java:/comp/env/jdbc/readingListDS  
```  

=> spring.datasource.jndi-name 프로퍼티를 설정하면 다른 DS 커넥션 프로퍼티는 무시  

### 3.2.2 외부에서 애플리케이션 빈 구성하기






































<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />  

---

# ch01 스프링 시작하기

### Index  

- <a href="#1.1">1.1 스프링의 새로운 시작</a>
- <a href="#1.2">1.2 스프링 부트 시작하기</a>

<div id="1.1"></div>  

## 1.1 스프링의 새로운 시작  

- 스프링은 무거운 엔터프라이즈 자바 빈(Enterprise Java Beans, EJB)으로 컴포넌트를 개발하지 않음  
=> 의존성 주입 + AOP를 활용해 EJB의 기능을 평범한 자바 객체(Plain Old Java Objects, POJO)로 구현
- 컴포넌트 코드 작성은 가벼웠으나, 개발 구성은 무거움(XML -> 어노테이션 -> Type-Safe 등)  
- 여전히 복잡한 구성을 벗어나지 못함  
- 의존성 관리의 문제  

### 1.1.1 스프링의 새로운 모습 살펴보기  

> Spring으로 Hello World 웹 어플리케이션을 개발하려면 ?  

- 필요한 의존성을 비롯한 메이븐(or 그레이들) 빌드 파일이 완비된 프로젝트 구조  
(적어도 스프링 MVC와 서블릿 API)  
- 스프링의 DispatcherServlet을 선언한 web.xml 파일(WebApplicationInitializer) 구현  
- 스프링 MVC를 사용할 수 있는 스프링 구성
- HTTP 요청에 "Hello World"라고 응답할 컨트롤러 클래스
- 애플리케이션을 배포할 웹 어플리케이션 서버(톰캣 등)  

=> Hello World 기능을 개발하는 데 특화된 컨트롤러 하나뿐  
(나머지 항목은 보일러플레이트(Boilerplate))  

> 그루비로 작성한 app.groovy  

```
@RestController
class HelloController {
  @RequestMapping("/")
  def hello() {
    return "Hello World";
  }
}
```

```
$ spring run app.groovy  
```  

### 1.1.2 스프링 부트 핵심 살펴보기  

- 자동 구성 : 스프링 APP의 공통으로 필요한 애플리케이션 기능을 자동으로 구성
- 스타터 의존성 : 필요한 라이브러리를 빌드에 추가하는 것을 보장  
- 명령줄 인터페이스 : 스프링부트의 이 부가 기능을 이용하면 애플리케이션 코드만 작성  
해도 완전한 애플리케이션을 개발할 수 있지만, 기존 프로젝트 빌드 방식에는 필요 없는 기능  
- 액추에이터 : 스프링 부트 애플리케이션을 실행할 때 내부에서 어떤일이 일어나는지  
이해할 수 있음  

**자동구성**  
어떤 스프링 APP의 소스 코드에서든 APP의 특정한 지원 특징과 기능을 활성화하는  
자바 구성이나 XML 구성(or 둘다)을 볼 수 있음  

> E.G : JDBC + 내장 H2 데이터베이스  

```
@Bean
public JdbcTemplate jdbcTemplate(DataSource dataSource) {
  return new JdbcTemplate(dataSource);
}

@Bean
public DataSource dataSource() {
  return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript('schema.sql', 'data.sql')
            .build();
}
```  
=> 내장 데이터베이스와 JdbcTemplate을 사용하는 애플리케이션은 이런 메소드가 필요  
=> 보일러플레이트 구성  

==> 스프링 부트는 이런 공통 구성 시나리오를 자동으로 구성할 수 있음(2장)  

**스타터의존성**  
; 스프링 부트는 스타터 의존성 수단으로 프로젝트 의존성을 쉽게 관리  
(몇 가지 기능 정의 의존성에 따라 전이적 의존성(Transitive Dependency) 해결 방법을  
이용하여 공통으로 사용하는 라이브러리들을 모으는 조금은 특별한 메이븐(그레이들) 의존성)  

**명령줄 인터페이스**  
; 애플리케이션 코드만 작성해도 애플리케이션을 개발할 수 있음  

```
@RestController
class HelloController {
  @RequestMapping("/")
  def hello() {
    return "Hello World";
  }
}
```  
=> import문이 존재하지 않음  
=> 스프링 부트 CLI가 어떤 타입을 사용했는지 발견하여 이 타입이 작동할 수 있게  
클래스 패스에 알맞은 스타터 의존성을 추가  
=> 의존성이 클래스패스에 있으면 일련의 자동 구성이 일어나 HTTP 요청에  
컨트롤러가 응답할 수 있도록 DispatcherServlet과 스프링 MVC를 활성화  

**액추에이터(Actuator)**  
; 작동 중인 애플리케이션의 내부를 살펴볼 수 있는 기능 제공  
- 스프링 애플리케이션 컨텍스트에 구성된 빈  
- 스프링 부트의 자동구성으로 구성된 것  
- 애플리케이션에서 사용할 수 있는 환경 변수, 시스템 프로퍼티, 구성 프로퍼티  
명령줄 인자
- 애플리케이션에서 구동 중인 스레드의 현재 상태
- 최근에 처리된 HTTP 요청 정보
- 메모리 사용량, 가비지 컬렉션, 웹 요청, 데이터 소스 사용량 등 다양한 메트릭  

---

<div id="1.2"></div>

## 1.2 스프링 부트 시작하기  

### 1.2.1 스프링 부트 CLI 설치하기  

> Windows  

- http://repo.spring.io/release/org/springframework/boot/spring-boot-cli/  
- 환경변수(bin)  
```
C:\spring-1.5.9.RELEASE> spring --version
Spring CLI v1.5.9.RELEASE
```  

### + unix 계열  

### 1.2.2 Spring Initializr로 스프링 부트 프로젝트 구성하기  


**Spring Initializr의 웹 인터페이스 사용**  

- http://start.spring.io/  

**Spring Tool Suite, IntelliJ 사용**  

**스프링 부트 CLI에서 Initializr 사용**  

> 기본 구조를 갖춘 APP  
```$spring init```  

> 웹, JPA, Security의 스타터 의존성 사용  
```$spring init -dweb,jpa,security  ```  

> 그레이들로 빌드타입 지정  
```$spring init -dweb,jpa,security --build gradle```

> WAR 파일로 생성(or -packaging)  
```$spring init -dweb,jpa,security --build gradle -p war myapp```  

> help  
```$spring help init```  

> Spring Initializr 서비스의 지원 기능  
```$spring init --list```  


















<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

---

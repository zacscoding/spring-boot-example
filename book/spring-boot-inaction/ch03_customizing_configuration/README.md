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















<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />  

---

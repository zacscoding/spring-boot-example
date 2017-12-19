### Project Inialize

> Starter

- spring-boot 2.0
- DevTools
- Thymeleaf
- Web
- Lombok

> encoding(utf-8)

> application.properties

<pre>
spring.devtools.livereload.enabled=true
spring.thymeleaf.cache=false
</pre>

> auto restart

- ctrl + shift + a => Registry...  
=> compiler.automake.allow.when.app.running checked  
=> File -> Settings -> Compiler => Build project automatically checked

#### Expresion Basic Object(표현식 기본 객체)

- \#ctx
- \#vars
- \#locale
- \#httpServletRequest
- \#httpSession

#### Expression Utility Objects(표현식 유틸 객체)
- \#dates
- \#calendars
- \#numbers
- \#strings
- \#objects
- \#bools
- \#arrays
- \#lists
- \#sets
- \#maps
- \#aggregates
- \#messages

<pre>
&lt;div$gt; [[${#vars.result}]] &lt;/div$gt;
</pre>


#### Thymeleaf layout dialect 를 이용한 레이아웃 재사용

> Maven

```
<dependency>
  <groupId>nz.net.ultraq.thymeleaf</groupId>
  <artifactId>thymeleaf-layout-dialect</artifactId>
  <version>2.2.2</version>
</dependency>
```

> static down & move to static dir
<a href="https://html5boilerplate.com/">html5-boilerplate</a>

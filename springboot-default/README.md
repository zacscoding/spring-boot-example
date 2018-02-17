## Spring Boot Basic Learn!!


#### index

- <a href="#schedule"> Scheduler <a>
- <a href="#property-files">Property files</a>
- <a href="#jsp">JSP</a>  





#### Schedule

<table>
    <tr>
        <td> app </td>
        <td>
            - org.boot.DemoApplication  
        </td>
    </tr>
    <tr>
        <td> Scheduler </td>
        <td>
            - org.boot.scheduler.ScheduledTasks
        </td>                    
    </tr>
    <tr>
        <td> </td>
        <td>
        </td>                    
    </tr>    
</table>

---

### Property files  

https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html  


```
Mode                LastWriteTime         Length Name
----                -------------         ------ ----
-a----     2018-02-17   오후 3:56             16 application.properties
-a----     2018-02-17   오후 3:55       15851197 springboot-default-0.0.1-SNAPSHOT.war
```

```
java -jar springboot-default-0.0.1-SNAPSHOT.war --spring.config.location=application.properties
```

---

### JSP   

1. build.gradle   

```
dependencies {
    ...
    // for jsp
    compile('javax.servlet:jstl')
    compile('org.apache.tomcat.embed:tomcat-embed-jasper')
    ...    
}
```

2. pom.xml

3. application.yml  

```  
...
spring:
  mvc:
    view:
      prefix: /WEB-INF/jsp/
      suffix: .jsp
...
```

4. Controller  

```
package org.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jsp/**")
@Slf4j
public class JspTestController {

    @GetMapping("/home")
    public String home(Model model) {
        log.info("## request home page");
        model.addAttribute("title", "SpringBoot JSP test");
        return "home";
    }
}

```  

5. JSP  

> webapp/WEB-INF/jsp/home.jsp  


6. test  

http://localhost:8080/boot-demo/jsp/home  







---



<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>



<table>
    <tr>
        <td> </td>
        <td>
        </td>                    
    </tr>
    <tr>
        <td> </td>
        <td>
        </td>                    
    </tr>
    <tr>
        <td> </td>
        <td>
        </td>                    
    </tr>    
</table>

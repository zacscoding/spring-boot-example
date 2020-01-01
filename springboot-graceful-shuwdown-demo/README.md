# Spring boot with graceful shutdown  
; this is a demo project for graceful shutdown

## Getting started  

> start and stop server

```aidl
$ ./gradlew bootRun

$ kill -15 `cat "./application.pid"` >/dev/null 2>&1
```

> test wait termination of protocol handler i.e tomcat's ThreadPoolExecutor

```aidl
$ curl -XGET http://localhost:8080/long-process  
```  

> result  

```log
INFO  o.s.s.c.ThreadPoolTaskExecutor - Shutting down ExecutorService
INFO  demo.GracefulShutdown - onApplicationEvent:protocol handler's executor : org.apache.tomcat.util.threads.ThreadPoolExecutor
INFO  demo.AppController - working ... http-nio-8080-exec-2-2
INFO  demo.AppController - working ... http-nio-8080-exec-2-3
INFO  demo.AppController - working ... http-nio-8080-exec-2-4
INFO  demo.AppController - working ... http-nio-8080-exec-2-5
INFO  demo.AppController - working ... http-nio-8080-exec-2-6
INFO  demo.AppController - working ... http-nio-8080-exec-2-7
INFO  demo.AppController - working ... http-nio-8080-exec-2-8
INFO  demo.AppController - working ... http-nio-8080-exec-2-9
INFO  demo.AppController - Complete task.
INFO  o.s.s.c.ThreadPoolTaskExecutor - Shutting down ExecutorService 'taskExecutor'
```  

> test wait termination of a TaskExecutor bean's task

```aidl
$ curl -XGET http://localhost:8080/tasks
```  

> result  

```aidl
INFO  o.s.s.c.ThreadPoolTaskExecutor - Shutting down ExecutorService
INFO  demo.GracefulShutdown - onApplicationEvent:protocol handler's executor : org.apache.tomcat.util.threads.ThreadPoolExecutor
INFO  o.s.s.c.ThreadPoolTaskExecutor - Shutting down ExecutorService 'taskExecutor'
INFO  demo.AppController - working ... taskExecutor-2-3
INFO  demo.AppController - working ... taskExecutor-1-3
INFO  demo.AppController - working ... taskExecutor-2-4
INFO  demo.AppController - working ... taskExecutor-1-4
INFO  demo.AppController - working ... taskExecutor-2-5
INFO  demo.AppController - working ... taskExecutor-1-5
INFO  demo.AppController - working ... taskExecutor-2-6
INFO  demo.AppController - working ... taskExecutor-1-6
INFO  demo.AppController - working ... taskExecutor-2-7
INFO  demo.AppController - working ... taskExecutor-1-7
INFO  demo.AppController - working ... taskExecutor-2-8
INFO  demo.AppController - working ... taskExecutor-1-8
INFO  demo.AppController - working ... taskExecutor-2-9
INFO  demo.AppController - working ... taskExecutor-1-9
INFO  demo.AppController - Complete task.
INFO  demo.AppController - Complete task.
```

---  

#### Configure protocol handler i.e tomcat's TaskExecutor  

> GracefulShutDown.java  

```java
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private static final int TIMEOUT_SECONDS = 10;

    private volatile Connector connector;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.connector.pause();
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        logger.info("onApplicationEvent:protocol handler's executor : {}", executor.getClass().getName());
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    logger.warn("Tomcat thread pool did not shut down gracefully within "
                                + TIMEOUT_SECONDS + " seconds. Proceeding with forceful shutdown");

                    threadPoolExecutor.shutdownNow();

                    if (!threadPoolExecutor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                        logger.error("Tomcat thread pool did not terminate");
                    }
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
``` 

> AppConfiguration.java  


```java
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfiguration {

    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(gracefulShutdown());
        return factory;
    }

    ...
}
```  

---  

#### Configure TaskExecutor with bean  

> AppConfiguration.java  


```java
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfiguration {
    ...

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(10);
        taskExecutor.initialize();

        return taskExecutor;
    }
}
```  

#### Configure TaskExecutor with not bean  
; adds Runtime's shutdown hook

> AppController.java  

```java
@RestController
@Slf4j
public class AppController {

    private final TaskExecutor taskExecutor;
    private final ThreadPoolTaskExecutor notBeanTaskExecutor;

    @Autowired
    public AppController(@Qualifier("taskExecutor") TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        notBeanTaskExecutor = new ThreadPoolTaskExecutor();
        notBeanTaskExecutor.setCorePoolSize(2);
        notBeanTaskExecutor.setMaxPoolSize(2);
        notBeanTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        notBeanTaskExecutor.setAwaitTerminationSeconds(10);
        notBeanTaskExecutor.initialize();
        Runtime.getRuntime().addShutdownHook(new Thread(notBeanTaskExecutor::shutdown));
    }

    ...
}
```



## Reference  

- https://blog.marcosbarbero.com/graceful-shutdown-spring-boot-apps/
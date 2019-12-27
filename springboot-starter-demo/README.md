# Create a {some modules}-spring-boot-starter i.e autoconfigure
This is a demo project for autoconfigure i.e some modules's spring boot starter.  

Suppose that we have a core module(`starter-core`) and then we want to use core module  

in spring boot server(`starter-server`) like below.  

```yaml
# The key "starter" is properties of core modules
starter:
  maxSize: 5
  maxTime: 3s
```  

Core module simply have random value buffer like below.  

> RandomValueBuffer in core module  

```java
@Slf4j
public class RandomValueBuffer {

    // required
    private final Optional<RandomValueListener> listenerOptional;

    // build
    private final FluxSink<String> idFluxSink;
    private final Flux<String> idFlux;

    // after started
    private Disposable subscription;

    public RandomValueBuffer(RandomValueListener listener) {
        listenerOptional = Optional.ofNullable(listener);

        final EmitterProcessor<String> emitterProcessor = EmitterProcessor.create();
        idFluxSink = emitterProcessor.sink(OverflowStrategy.BUFFER);
        idFlux = emitterProcessor.publishOn(Schedulers.elastic());
    }

    public void start(int maxSize, Duration maxTime) {
        if (subscription != null) {
            logger.warn("Already RandomValueBuffer is started");
            return;
        }

        logger.info("Start random value buffer with max size : {} / max time : {}", maxSize, maxTime);

        subscription = idFlux.bufferTimeout(maxSize, maxTime, Schedulers.elastic())
                             .subscribe(values -> {
                                 System.out.println(">> Flux random id buffer");
                                 System.out.println(String.join("\n", values));
                                 listenerOptional.ifPresent(listener -> listener.onValues(values));
                             });
    }

    public void stop() {
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
            logger.debug("Success to stop RandomValueBuffer");
        }
    }

    public void publishRandomId(String id) {
        idFluxSink.next(id);
    }
}
```  

> RandomValueListener in core module  

```java
public interface RandomValueListener {

    void onValues(List<String> values);
}
```  

And then we want to use `RandomValueBuffer` bean by using spring-boot-starter i.e autoconfigure.  

---  

> ## Getting started  

```aidl
$ ./mvnw clean build
$ java -jar starter-server/build/libs/starter-server-0.0.1.jar  
... running server ...

// publish UUID 5 times and then flush buffer immediately
// because our buffer's max size is 5.
$ curl -XGET http://localhost:3000/push?repeat=5

// publish UUID 4 time and then flush after 3 seconds.
// because out buffer's max size is 5 and max time is 3s.
$ curl -XGET http://localhost:3000/push?repeat=4
```  

---

> ## How to autoconfigure(see starter module)  

#### 1. Create a file in ur resources/META-INF/spring.factories in starter module  

```text
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
demo.starter.spring.StarterAutoConfiguration
```  

#### 2. Create a AutoConfiguration in starter module  

> StarterAutoConfiguration.java

```java
import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.starter.core.RandomValueBuffer;
import demo.starter.core.RandomValueListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(RandomValueBuffer.class)
@EnableConfigurationProperties(StarterProperties.class)
@Slf4j
@RequiredArgsConstructor
public class StarterAutoConfiguration {

    private final StarterProperties properties;

    @PostConstruct
    private void setUp() {
        logger.info("Enable starter auto configuration. {}", properties);
    }

    // create a RandomValueListener bean if not exist
    @Bean
    @ConditionalOnMissingBean(RandomValueListener.class)
    public RandomValueListener randomValueListener() {
        logger.info("## Create a RandomValueListener bean");
        return values -> logger.info("onNewValues with size : {}", values.size());
    }

    // create a RandomValueBuffer bean if not exist
    @Bean
    @ConditionalOnMissingBean(RandomValueBuffer.class)
    public RandomValueBuffer buffer(RandomValueListener listener) {
        logger.info("## Create a RandomValueBuffer bean");

        final RandomValueBuffer buffer = new RandomValueBuffer(listener);

        buffer.start(properties.getMaxSize(), properties.getMaxTime());

        return buffer;
    }
}
```  

> StarterProperties.java  

```java
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(prefix = "starter")
@Getter
@Setter
@ToString
public class StarterProperties {

    private int maxSize;
    private Duration maxTime;
}
```

#### 3. Use properties in server module  

> application.yaml  

```yaml
server:
  port: 3000

starter:
  maxSize: 5
  maxTime: 3s
```




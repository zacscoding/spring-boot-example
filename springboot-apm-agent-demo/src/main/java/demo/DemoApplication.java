package demo;

import com.fasterxml.classmate.TypeResolver;
import demo.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@SpringBootApplication
@EnableSwagger2
@Configuration
public class DemoApplication {

    /*
    addes vm args

    -javaagent:lib\elastic-apm-agent-1.3.0.jar
    -Delastic.apm.service_name=my-cool-service
    -Delastic.apm.application_packages=demo.person
    -Delastic.apm.server_urls=http://127.0.0.1:8080
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public Docket api(TypeResolver resolver) {
        return new Docket(DocumentationType.SWAGGER_2)
            .additionalModels(resolver.resolve(Person.class))
            .select()
            // .apis(RequestHandlerSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("demo.person"))
            // .paths(PathSelectors.any())
            .paths(PathSelectors.ant("/person/**"))
            .build();
    }
}


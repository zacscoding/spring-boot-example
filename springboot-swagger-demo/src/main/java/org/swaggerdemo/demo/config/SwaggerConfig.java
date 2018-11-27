package org.swaggerdemo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * http://localhost:8080/swagger-ui.html
 *
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      // .apis(RequestHandlerSelectors.any())
                                                      .apis(RequestHandlerSelectors.basePackage("org.swaggerdemo.demo.controller"))
                                                      // .paths(PathSelectors.any())
                                                      .paths(PathSelectors.ant("/person/**")).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Spring boot Swagger Demo").description("This is demo app").build();
    }

}

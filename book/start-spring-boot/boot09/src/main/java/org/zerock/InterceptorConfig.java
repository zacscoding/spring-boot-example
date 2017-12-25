package org.zerock;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zerock.interceptor.LoginCheckInterceptor;

/**
 * @author zacconding
 * @Date 2017-12-25
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@Log
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/login");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}

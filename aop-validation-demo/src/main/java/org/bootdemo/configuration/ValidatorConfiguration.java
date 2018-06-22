package org.bootdemo.configuration;

import org.bootdemo.validator.PageRequestRule;
import org.bootdemo.validator.ParameterRule;
import org.bootdemo.validator.TempRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class ValidatorConfiguration {

    @Bean(name = "pageRequestRule")
    public ParameterRule pageRequestRule() {
        return new PageRequestRule();
    }

    @Bean(name = "tempRule")
    public ParameterRule tempRule() {
        return new TempRule();
    }
}

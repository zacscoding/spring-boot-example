package demo.lazybean;

import demo.util.ProfileNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-08-28
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "demo.lazybean")
@Profile(ProfileNames.LAZY_LOAD)
public class LazyBeanConfiguration {

    @Bean
    @Lazy
    public LazyBeanService1 lazyBeanService1() {
        log.info("## lazyBeanService1() is called in LazyBeanConfiguration");
        return new LazyBeanService1();
    }

    @Bean
    @Lazy
    public LazyBeanService2 lazyBeanService2() {
        log.info("##  lazyBeanService2() is called in LazyBeanConfiguration");
        return new LazyBeanService2();
    }
}
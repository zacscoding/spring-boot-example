package demo.appctx;

import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-08-29
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@ComponentScan(basePackages = "demo.appctx")
@Configuration
@Profile("appctx")
public class GetBeansConfiguration {

    @Autowired
    private ApplicationContext ctx;

    @PostConstruct
    private void setUp() {
        log.info("## GetBeansConfiguration() is called");
        Map<String, DaemonMaker> beans = ctx.getBeansOfType(DaemonMaker.class);
        log.info(">>>>>>>>>>>>>>>>>>>>> Check daemon... size : " + beans.size());

        for (Entry<String, DaemonMaker> entry : beans.entrySet()) {
            log.info("> Key : {} ==> Name : {}", entry.getKey(), entry.getValue().getName());
        }
    }
}
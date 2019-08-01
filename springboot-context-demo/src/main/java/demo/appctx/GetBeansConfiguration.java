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
        logger.info("## GetBeansConfiguration() is called");
        Map<String, DaemonMaker> beans = ctx.getBeansOfType(DaemonMaker.class);
        logger.info(">>>>>>>>>>>>>>>>>>>>> Check daemon... size : " + beans.size());

        for (Entry<String, DaemonMaker> entry : beans.entrySet()) {
            logger.info("> Key : {} ==> Name : {}", entry.getKey(), entry.getValue().getName());
        }
    }
}

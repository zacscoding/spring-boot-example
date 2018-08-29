package demo.appctx;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-08-29
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
@Profile("appctx")
public class AppCtxBean1 implements DaemonMaker {

    @PostConstruct
    private void setUp() {
        log.info("AppCtxBean1 is called");
    }

    @Override
    public String getName() {
        return "AppCtxBean1";
    }
}
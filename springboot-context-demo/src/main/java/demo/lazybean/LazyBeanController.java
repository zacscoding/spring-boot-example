package demo.lazybean;

import demo.util.ProfileNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-08-28
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@Profile(ProfileNames.LAZY_LOAD)
public class LazyBeanController {

    @Autowired
    private LazyBeanService1 lazyBeanService1;

    @Autowired
    private ApplicationContext context;

    @GetMapping("/lazybean/bean1")
    public String call() {
        log.info("## /lazybean/test1 is called in LazyBeanController");
        return lazyBeanService1.getName();
    }

    @GetMapping("/lazybean/bean2")
    public String call2() {
        log.info("## /lazybean/test2 is called in LazyBeanController");
        LazyBeanService2 bean = context.getBean(LazyBeanService2.class);
        return bean.getName();
    }

    @GetMapping("/lazybean/bean3")
    public String call3() {
        log.info("## /lazybean/test3 is called in LazyBeanController");
        LazyBeanService3 bean = context.getBean(LazyBeanService3.class);
        return bean.getName();
    }
}
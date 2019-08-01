package demo.lazybean;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-08-29
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@Lazy
public class LazyBeanController2 {

    @PostConstruct
    public void setUp() {
        logger.info("## LazyBeanController2 is called in LazyBeanController2::setUp()");
    }

    @GetMapping("/lazybean2")
    public String getName1() {
        return ":)";
    }
}

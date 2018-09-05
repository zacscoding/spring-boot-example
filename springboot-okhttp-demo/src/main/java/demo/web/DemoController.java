package demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-09-05
 * @GitHub : https://github.com/zacscoding
 */
@RestController
@Slf4j
public class DemoController {

    @GetMapping("/temp")
    public String temp() {
        log.info("## request temp...");
        return "temp for demo";
    }
}
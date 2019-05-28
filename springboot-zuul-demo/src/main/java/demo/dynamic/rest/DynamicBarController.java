package demo.dynamic.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("dynamic-bar")
public class DynamicBarController {

    @GetMapping
    public String getName() {
        logger.info("DynamicBarController:getName() is called");
        return "Bar";
    }
}

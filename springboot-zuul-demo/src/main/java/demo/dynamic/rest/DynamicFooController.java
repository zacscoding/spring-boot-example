package demo.dynamic.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mock endpoint
 *
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("dynamic-foo")
public class DynamicFooController {

    @GetMapping
    public String getName() {
        logger.info("DynamicFooController:getName() is called");
        return "Foo";
    }
}

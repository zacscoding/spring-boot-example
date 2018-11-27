package org.swaggerdemo.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@RestController
@RequestMapping("/not-api/**")
public class NotAPIController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}

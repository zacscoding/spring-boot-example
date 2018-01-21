package org.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@RestController
public class FrontController {
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
    @GetMapping(value="/echo/{message}")
    public String echo(@PathVariable("message") String message) {
        logger.info("## [request echo] message : {}", message);
        return message;
    }
}

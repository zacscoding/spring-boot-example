package org.zaccoding.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * @author zacconding
 * @Date 2018-01-20
 * @GitHub : https://github.com/zacscoding
 */
@Controller
public class FrontController {

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/list-op/{key}/{value}")
    @ResponseBody
    public String index(@PathVariable("key") String key, @PathVariable("value") String value) {
        logger.info("## [request hello] key : {}, value : {}", key, value);
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        listOperations.rightPush(key, value);
        Set<String> keys = stringRedisTemplate.keys("*");
        logger.info("## keys size : {}", keys.size());
        StringBuilder sb = new StringBuilder();
        for (String k : keys) {
            sb.append("========  ").append(k).append("  ======== <br />");
            listOperations.range(key, 0, -1).forEach(v -> {
                sb.append(v).append(" <br />");
            });
        }
        sb.append("======================================== <br />");
        return sb.toString();
    }
}

package org.bootdemo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.bootdemo.annotation.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-06-21
 * @GitHub : https://github.com/zacscoding
 */
@RestController
@RequestMapping("/**")
@Slf4j
public class IndexController {

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/map")
    public String mapRequest(@RequestBody Map<String, Object> param) {
        return toJsonString(param);
    }

    @RequestMapping(value = "/anno")
    @PreFilter(argRules = {"tempRule,pageRequestRule"})
    public String annoRequest(@RequestBody Map<String, Object> param) {
        return toJsonString(param);
    }

    private String toJsonString(Object inst) {
        if (inst == null) {
            return null;
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inst);
        } catch (Exception e) {
            return "[Error] : " + e.getMessage();
        }
    }
}
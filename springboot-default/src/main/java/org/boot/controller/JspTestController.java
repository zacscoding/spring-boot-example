package org.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zacconding
 * @Date 2018-02-17
 * @GitHub : https://github.com/zacscoding
 */
@Controller
@RequestMapping("/jsp/**")
@Slf4j
public class JspTestController {

    @GetMapping("/home")
    public String home(Model model) {
        log.info("## request home page");
        model.addAttribute("title", "SpringBoot JSP test");
        return "home";
    }
}

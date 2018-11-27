package org.zerock.controller;

import lombok.extern.java.Log;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zacconding
 * @Date 2017-12-24
 * @GitHub : https://github.com/zacscoding
 */
@Controller
@Log
public class SampleController {

    @GetMapping("/")
    public String index() {
        log.info("## request index page");
        return "index";
    }

    @RequestMapping("/guest")
    public void forGuest() {
        log.info("## request guest page");
    }

    @RequestMapping("/manager")
    public void forManager() {
        log.info("## request manager page");
    }

    @RequestMapping("/admin")
    public void forAdmin() {
        log.info("## request admin page");
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping("/adminSecret")
    public void forAdminSecret() {
        log.info("## request forAdminSecret()");
    }

    @Secured({"ROLE_MANAGER"})
    @RequestMapping("/managerSecret")
    public void forManagerSecret() {
        log.info("## request forManagerSecret()");
    }

}

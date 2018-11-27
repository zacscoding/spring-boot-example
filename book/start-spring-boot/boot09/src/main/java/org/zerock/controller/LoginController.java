package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zacconding
 * @Date 2017-12-24
 * @GitHub : https://github.com/zacscoding
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public void login() {

    }

    @GetMapping("/logout")
    public void logout() {

    }

    @GetMapping("/accessDenied")
    public void accessDenied() {

    }


}

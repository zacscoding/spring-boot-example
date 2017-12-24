package org.zerock.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.domain.Member;
import org.zerock.persistence.MemberRepository;
import sun.security.util.Password;

/**
 * @author zacconding
 * @Date 2017-12-24
 * @GitHub : https://github.com/zacscoding
 */
@Controller
@RequestMapping("/member/")
@Log
public class MemberClass {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;

    @GetMapping("/join")
    public void join() {

    }

    @PostMapping("/join")
    public String joinPost(@ModelAttribute("member") Member member) {
        log.info("## join post request : " + member);

        String encryptPw = passwordEncoder.encode(member.getUpw());
        member.setUpw(encryptPw);

        memberRepository.save(member);

        return "/member/joinResult";
    }


}




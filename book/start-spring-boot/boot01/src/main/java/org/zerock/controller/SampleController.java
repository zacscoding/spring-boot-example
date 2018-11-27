package org.zerock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;

import java.lang.reflect.Method;

/**
 * @author zacconding
 * @Date 2017-12-16
 * @GitHub : https://github.com/zacscoding
 */

// spring-boot에서는 별도의 <component-scan> 없이 빈 설정(패키지가 다르면 주의)
// App 클래스에서 @ComponentScan으로 이용 가능.
@RestController
public class SampleController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World";
    }

    @GetMapping("/sample")
    public SampleVO makeSampe() {
        SampleVO vo = new SampleVO();
        vo.setVal1("v1");
        vo.setVal2("v2");
        vo.setVal3("v3");

        Method[] methods = vo.getClass().getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(String.format("## exist method : [%s] in SampleVO", method.toString()));
        }

        System.out.println("## vo : " + vo);
        return vo;
    }

}

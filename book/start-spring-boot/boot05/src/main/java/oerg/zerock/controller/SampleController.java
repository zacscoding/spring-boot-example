package oerg.zerock.controller;

import oerg.zerock.domain.MemberVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author zacconding
 * @Date ${date}
 * @GitHub : https://github.com/zacscoding
 */
@Controller
public class SampleController {
    @GetMapping("/sample1")
    public void sample1(Model model) {
        System.out.println("## sample1() request");
        // model.addAttribute("greeting","Hello World!!");
        model.addAttribute("greeting","안녕하세요");
    }

    @GetMapping("/sample2")
    public void sample2(Model model) {
        System.out.println("## sample2() request");
        MemberVO vo = new MemberVO(123,"u00","p00","홍길동", new Timestamp(System.currentTimeMillis()));

        model.addAttribute("vo",vo);
    }

    @GetMapping("/sample3")
    public void sample3(Model model) {
        System.out.println("## sample3() request");
        List<MemberVO> members = new ArrayList<>();

        IntStream.range(1,11).forEach(i -> {
            members.add(new MemberVO(123,"u0" + i,"p0" + i,"홍길동" + i, new Timestamp(System.currentTimeMillis())));
        });

        model.addAttribute("members", members);
    }

    @GetMapping("/sample4")
    public void sample4(Model model) {
        System.out.println("## sample4() request");
        List<MemberVO> members = new ArrayList<>();

        IntStream.range(1,11).forEach(i -> {
            members.add(new MemberVO(123,"u000" + i%3,"p0000" + i%3,"홍길동" + i, new Timestamp(System.currentTimeMillis())));
        });

        model.addAttribute("members", members);
    }

    @GetMapping("/sample5")
    public void sample5(Model model) {
        System.out.println("## sample5() request");

        String result = "SUCCESS";

        model.addAttribute("result",result);
    }

    @GetMapping("/sample6")
    public void sample6(Model model) {
        System.out.println("## sample6() request");
        List<MemberVO> members = new ArrayList<>();

        IntStream.range(1,11).forEach(i -> {
            members.add(new MemberVO(123,"u0" + i,"p0" + i,"홍길동" + i, new Timestamp(System.currentTimeMillis())));
        });

        model.addAttribute("members", members);

        String result = "SUCCESS";
        model.addAttribute("result",result);
    }

    @GetMapping("/sample7")
    public void sample7(Model model) {
        System.out.println("## sample7() request");

        model.addAttribute("now", new Date());
        model.addAttribute("price", 123456789 );
        model.addAttribute("title", "This is a just sample." );
        model.addAttribute("options", Arrays.asList("AAAA", "BBB", "CCC", "DDD") );
    }

    @GetMapping("/sample8")
    public void sample8(Model model) {

    }

    @GetMapping("/sample/hello")
    public void hello() {

    }
}

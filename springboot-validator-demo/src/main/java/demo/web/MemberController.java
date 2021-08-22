package demo.web;

import demo.dto.MemberRequest;
import demo.dto.MemberResponse;
import demo.service.MemberService;
import demo.util.GsonUtil;
import lombok.Data;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://jojoldu.tistory.com/129
 */
@Controller
@RestController
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger("member");

    @Autowired
    private MemberService memberService;

    @PostMapping(value = "/member")
    public Long saveMember(@RequestBody @Valid MemberRequest memberRequest) {
        logger.debug("Save member request : \n{}", GsonUtil.toStringPretty(memberRequest));

        return memberService.save(memberRequest);
    }

    @GetMapping(value = "/members")
    public List<MemberResponse> getMembers() {
        return memberService.findAll();
    }
}

package jpabook.jpashop.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    // required
    private final ModelMapper modelMapper;
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        logger.info("createForm is called");
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        logger.info("create [POST] is called. member form : {}", form);

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        final Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        final Member member = modelMapper.map(form, Member.class);
        member.setAddress(address);

        Long memberId = memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        // TODO : Member entity를 직접 사용하기 보다는 DTO를 반환
        logger.info("called [GET] members");
        final List<Member> members = memberService.findMembers();

        model.addAttribute("members", members);

        return "members/memberList";
    }
}

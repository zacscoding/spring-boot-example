package datajpa.controller;

import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import datajpa.dto.MemberDto;
import datajpa.entity.Member;
import datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMembers(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> getMembers(@PageableDefault(size = 5) Pageable pageable) {
        return memberRepository.findAll(pageable)
                               .map(member -> new MemberDto(member.getId(), member.getUsername(), null));
    }

    @PostConstruct
    private void init() {
        IntStream.range(1, 100).forEach(i -> {
            memberRepository.save(new Member("user" + i, i));
        });
    }
}

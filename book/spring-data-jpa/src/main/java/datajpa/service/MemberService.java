package datajpa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import datajpa.entity.Member;
import datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = false)
    public Long saveMember(Member member) {
        System.out.println("MemberService::saveMember is called");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (StackTraceElement elt : stackTrace) {
            System.out.println(elt);
        }

        return memberRepository.save(member).getId();
    }
}

package demo.service;

import demo.dto.MemberRequest;
import demo.dto.MemberResponse;
import demo.entity.Member;
import demo.exception.ValidationCustomException;
import demo.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * http://jojoldu.tistory.com/129
 */
@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger("member");

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long save(MemberRequest memberRequest) {
        verifyDuplicateEmail(memberRequest.getEmail());
        return memberRepository.save(memberRequest.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream().map(MemberResponse::new).collect(Collectors.toList());
    }

    private void verifyDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new ValidationCustomException("Already exist email.", "email");
        }
    }
}

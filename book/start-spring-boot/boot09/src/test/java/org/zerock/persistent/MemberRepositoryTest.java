package org.zerock.persistent;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Member;
import org.zerock.domain.MemberRole;
import org.zerock.persistence.MemberRepository;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author zacconding
 * @Date 2017-12-24
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void insert() {
        memberRepository.deleteAll();

        IntStream.range(0, 101).forEach(i -> {
            Member member = new Member();
            member.setUid("user" + i);
            // member.setUpw("pw"+i);
            member.setUpw(passwordEncoder.encode("pw" + i));
            member.setUname("사용자" + i);

            MemberRole role = new MemberRole();
            if (i <= 80) {
                role.setRoleName("BASIC");
            } else if (i <= 90) {
                role.setRoleName("MANAGER");
            } else {
                role.setRoleName("ADMIN");
            }
            member.setRoles(Arrays.asList(role));
            memberRepository.save(member);
        });
    }

    //@Transactional
    @Test
    public void read() {
        String uid = "user85";
        Optional<Member> result = memberRepository.findById(uid);
        result.ifPresent(member -> {
            log.info("## member : " + member);
        });
    }

}

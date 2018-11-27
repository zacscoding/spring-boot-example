package org.zerock.persistence;

import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Member;
import org.zerock.domain.Profile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit // 테스트 결과 커밋
public class ProfileTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProfileRepository profileRepository;

    @Test
    @Ignore
    public void insertMembers() {
        IntStream.range(1, 101).forEach(i -> {
            Member member = new Member();
            member.setUid("user" + i);
            member.setUpw("pw" + i);
            member.setUname("사용자" + i);
            memberRepository.save(member);
        });
    }

    @Test
    @Ignore
    public void insertProfiles() {
        Member member = new Member();
        member.setUid("user1");

        for (int i = 1; i < 5; i++) {
            Profile profile = new Profile();
            profile.setFname("face" + i + ".jpg");
            if (i == 1) {
                profile.setCurrent(true);
            }

            profile.setMember(member);
            profileRepository.save(profile);
        }
    }

    @Test
    public void testFetchJoin1() {
        List<Object[]> results = memberRepository.getMemberWithProfileCount("user1");
        results.forEach(arr -> {
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testFetchJoin2() {
        List<Object[]> results = memberRepository.getMemberWithProfiles("user1");
        results.forEach(arr -> {
            System.out.println(Arrays.toString(arr));
        });
    }


}

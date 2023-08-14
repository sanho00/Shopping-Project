package com.shopping;

import com.shopping.domain.Member;
import com.shopping.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DbApplicationTest {

    private final MemberRepository memberRepository;

    //의존성 주입
    @Autowired
    public DbApplicationTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    void memberAdd() {
        Member member = new Member();
        member.setName("Spring");
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
    }
}

package com.shopping.service;

import com.shopping.domain.Member;
import com.shopping.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional // Write(insert, update, delete)
    public Member signup(Member member) {
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);
        member.setRole(member.getRole()); // 기본 member 권한

        Member memberEntity = memberRepository.save(member);

        return memberEntity;
    }

//    @Transactional
//    public Member memberUpdate(Member member) {
//        Member memberEntity = memberRepository.save(member);
//        return memberEntity;
//    }

}

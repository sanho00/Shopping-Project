package com.shopping.service;

import com.shopping.domain.Member;
import com.shopping.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPageService {

    private final MemberRepository memberRepository;

    // ID 로 Member 찾기
    public Member findMember(Integer id) {
        return memberRepository.findById(id).get();
    }

    // 회원 정보 수정
    public void memberModify(Member member) {
        Member modify = memberRepository.findById(member.getId());
        modify.setUsername(member.getUsername());
        modify.setEmail(member.getEmail());
        modify.setAddress(member.getAddress());
        modify.setPhoneNumber(member.getPhoneNumber());
        memberRepository.save(modify);
    }
}

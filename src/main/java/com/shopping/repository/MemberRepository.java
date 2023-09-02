package com.shopping.repository;

import com.shopping.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findById(int id);
    Member findByUsername(String username);
    Member findByEmail(String email);
    List<Member> findAll();

}

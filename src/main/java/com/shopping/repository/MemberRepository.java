package com.shopping.repository;

import com.shopping.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);
    Member findByUsername(String username);
    List<Member> findAll();

}

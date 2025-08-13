package com.mysite.sbb.member.repository;

import com.mysite.sbb.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByUsername(String username); // 사용자 이름(ID)으로 회원 조회
}

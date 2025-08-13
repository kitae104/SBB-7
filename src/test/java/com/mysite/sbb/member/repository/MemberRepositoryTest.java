package com.mysite.sbb.member.repository;

import com.mysite.sbb.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void createMember(){
    Member member = Member.builder()
        .username("aaa")
        .email("aaa@test.com")
        .password(passwordEncoder.encode("1111"))
        .build();
    Member savedMember = memberRepository.save(member);
    assertEquals("aaa", savedMember.getUsername());
  }
}
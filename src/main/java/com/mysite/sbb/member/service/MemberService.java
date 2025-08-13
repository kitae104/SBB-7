package com.mysite.sbb.member.service;

import com.mysite.sbb.common.exception.DataNotFoundException;
import com.mysite.sbb.member.dto.MemberFormDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public Member create(String username, String email, String password) {
    Member member = new Member();
    member.setUsername(username);
    member.setEmail(email);
    //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // SecurityConfig 에서 설정
    member.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
    Member savedMember = memberRepository.save(member);
    return savedMember;
  }

  public void create(@Valid MemberFormDto memberFormDto) {
    Member member = new Member();
    member.setUsername(memberFormDto.getUsername());
    member.setEmail(memberFormDto.getEmail());
    member.setPassword(passwordEncoder.encode(memberFormDto.getPassword1())); // 비밀번호 암호화
    memberRepository.save(member);
  }

  public Member getMember(String username) {
      Optional<Member> member = memberRepository.findByUsername(username);
      if (member.isPresent()) {
          return member.get();
      } else {
          throw new DataNotFoundException("사용자를 찾을 수 없습니다: " + username);
      }

//    return memberRepository.findByUsername(username)
//        .orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다: " + username));
  }
}

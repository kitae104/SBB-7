package com.mysite.sbb.member.controller;

import com.mysite.sbb.member.dto.MemberFormDto;
import com.mysite.sbb.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/signup")
  public String signUp(Model model) {
    model.addAttribute("memberFormDto", new MemberFormDto());
    return "member/signup"; // signup.html 파일을 반환
  }

  @PostMapping("/signup")
  public String signUp(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
    log.info("=====> memberFormDto: {}", memberFormDto);

    if (bindingResult.hasErrors()) {
      return "member/signup";
    }

    if (!memberFormDto.getPassword1().equals(memberFormDto.getPassword2())) {
      // 필드명, 오류 코드, 오류 메시지
      bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
      return "member/signup";
    }

    try {
//      memberService.create(memberFormDto.getUsername(), memberFormDto.getEmail(), memberFormDto.getPassword1());
      memberService.create(memberFormDto);

    } catch (DataIntegrityViolationException e) {
      log.info("=============================회원가입 실패: 이미 등록된 사용자입니다.");
//      e.printStackTrace();
//      bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
      model.addAttribute("errorMessage","이미 등록된 사용자입니다.");
      return "member/signup";
    } catch (Exception e) {
      log.info("=============================회원가입 실패: " + e.getMessage());
//      e.printStackTrace();
//      bindingResult.reject("signupFailed", e.getMessage());
      model.addAttribute("errorMessage", e.getMessage());
      return "member/signup";
    }

    return "redirect:/"; // 회원가입 성공 후 리다이렉트
  }

  @GetMapping("/login")
  public String login() {

    return "member/login";
  }

  @GetMapping(value = "/login/error")
  public String loginError(Model model){
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
    return "/member/login";
  }

  @GetMapping("/logout")
  public String performLogout(HttpServletRequest request, HttpServletResponse response) {
    log.info("===============> logout");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    return "redirect:/";
  }
}

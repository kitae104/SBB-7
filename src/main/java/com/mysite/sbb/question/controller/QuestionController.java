package com.mysite.sbb.question.controller;

import com.mysite.sbb.answer.dto.AnswerFormDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import com.mysite.sbb.question.dto.QuestionFormDto;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService;
  private final MemberService memberService;

  @GetMapping("/list")
  public String list(Model model,
                    @RequestParam(value = "page", defaultValue = "0") int page) {
    Page<Question> paging = questionService.getList(page);
    log.info("paging info = {}", paging);
    model.addAttribute("paging", paging);
    return "question/list";
  }

  @GetMapping("/detail/{id}")
  public String detail(@PathVariable("id") Long id, Model model,
                       AnswerFormDto answerFormDto) { // 답변 등록 기능 시 추가
    Question question = questionService.getQuestion(id);
    model.addAttribute("question", question);
    return "question/detail";
  }

  @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
  @GetMapping("/create")
  public String createQuestion(QuestionFormDto questionFormDto) {
    return "question/inputForm";
  }

  @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
  @PostMapping("/create")
  public String createQuestion(@Valid QuestionFormDto questionFormDto,
                               BindingResult bindingResult,
                               Principal principal) {

    if (bindingResult.hasErrors()) {
      return "question/inputForm";
    }

    Member member = memberService.getMember(principal.getName());

    // 필요시 예외 처리 추가
    questionService.create(questionFormDto, member) ;
    return "redirect:/question/list";
  }

  @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
  @GetMapping("/modify/{id}")
  public String modifyQuestion(@PathVariable("id") Long id, QuestionFormDto questionFormDto, Principal principal) {
    Question question = questionService.getQuestion(id);
    if(!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
    }
    questionFormDto.setSubject(question.getSubject());
    questionFormDto.setContent(question.getContent());
    return "question/inputForm";
  }

  @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
  @PostMapping("/modify/{id}")
  public String modifyQuestion(@PathVariable("id") Long id,
                               @Valid QuestionFormDto questionFormDto,
                               BindingResult bindingResult,
                               Principal principal) {
    if (bindingResult.hasErrors()) {
      return "question/inputForm";
    }

    Question question = questionService.getQuestion(id);
    if(!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
    }
    questionService.modify(question, questionFormDto.getSubject(), questionFormDto.getContent());
    return "redirect:/question/detail/" + id;
  }

  @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
  @GetMapping("/delete/{id}")
  public String deleteQuestion(@PathVariable("id") Long id, Principal principal) {
    Question question = questionService.getQuestion(id);
    if(!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
    }
    questionService.delete(question);
    return "redirect:/";
  }

  @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
  @GetMapping("/vote/{id}")
  public String voteQuestion(@PathVariable("id") Long id, Principal principal) {
    Question question = questionService.getQuestion(id);
    Member member = memberService.getMember(principal.getName());
    questionService.vote(question, member);
    return "redirect:/question/detail/" + id;
  }
}

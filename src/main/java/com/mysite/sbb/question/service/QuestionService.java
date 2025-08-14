package com.mysite.sbb.question.service;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.dto.QuestionFormDto;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.repository.QuestionRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

  private final QuestionRepository questionRepository;

  /////////////////////////////////////////////////////////////////
  // 검색 기능
  /////////////////////////////////////////////////////////////////
  private Specification<Question> search(String keyword){
    return new Specification<>(){

      @Override
      public Predicate toPredicate(Root<Question> question, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.distinct(true); // 중복 제거
        Join<Question, Member> member1 = question.join("author", JoinType.LEFT);
        Join<Question, Answer> answer = question.join("answerList", JoinType.LEFT);
        Join<Answer, Member> member2 =  answer.join("author", JoinType.LEFT);
        return criteriaBuilder.or(criteriaBuilder.like(question.get("subject"), "%" + keyword + "%"), // 제목
          criteriaBuilder.like(question.get("content"), "%" + keyword + "%"), // 내용
          criteriaBuilder.like(member1.get("username"), "%" + keyword + "%"), // 질문 작성자
          criteriaBuilder.like(member2.get("username"), "%" + keyword + "%"), // 답변 작성자
          criteriaBuilder.like(answer.get("content"), "%" + keyword + "%"));
      }
    };
  }

  public Page<Question> getList(int page, String keyword) {
    List<Sort.Order> sorts = new ArrayList<Sort.Order>();
    sorts.add(Sort.Order.desc("created")); // 생성일 기준 내림차순 정렬
    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 페이지당 10개씩 조회 + 정렬 기능
    Specification<Question> specification = search(keyword); // 검색 수행
    Page<Question> questionList = questionRepository.findAll(specification, pageable);
    return questionList;
  }

  public Question getQuestion(Long id) {
    Optional<Question> question = questionRepository.findById(id);
    if(question.isPresent()) {
      return question.get();
    } else {
      throw new IllegalArgumentException("id에 해당하는 질문이 없습니다 : " + id);
    }

    // 간략한 표현
//    Question question = questionRepository.findById(id)
//        .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));
//    return question;
  }

  public void create(QuestionFormDto questionFormDto, Member author) {
    Question question = Question.builder()
        .subject(questionFormDto.getSubject())
        .content(questionFormDto.getContent())
        .author(author) // 질문 작성자 설정
        .build();
    questionRepository.save(question);
  }

  public void modify(Question question, String subject, String content) {
    question.setSubject(subject);
    question.setContent(content);
    questionRepository.save(question);
  }

  public void delete(Question question) {
    questionRepository.delete(question);
  }

  public void vote(Question question, Member member) {
    question.getVoter().add(member); // 질문에 투표한 사용자 추가(집합에 추가)
    questionRepository.save(question);
  }
}

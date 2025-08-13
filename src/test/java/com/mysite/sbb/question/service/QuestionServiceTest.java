package com.mysite.sbb.question.service;

import com.mysite.sbb.question.dto.QuestionFormDto;
import com.mysite.sbb.question.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionServiceTest {

  @Autowired
  private QuestionService questionService;

  @Transactional
  @Test
  void createDumyData() {
    for (int i = 0; i < 300 ; i++) {
      QuestionFormDto questionFormDto = QuestionFormDto.builder()
          .subject("테스트 질문 " + i)
          .content("테스트 내용 " + i)
          .build();
//      questionService.create(questionFormDto);
    }
//    assertEquals(306, questionService.getList().size(), "질문 데이터가 300개가 되어야 합니다.");
  }
}
package com.mysite.sbb.answer.service;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.answer.repository.AnswerRespository;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {

  private final AnswerRespository answerRespository;

  public Answer create(Question question, String content, Member author) {
    Answer answer = Answer.builder()
        .content(content)
        .question(question)
        .author(author)
        .build();

    answerRespository.save(answer);
    return answer;
  }

  public Answer getAnswer(Long id) {
    Answer answer = answerRespository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다: " + id));
    return answer;
  }

  public void modify(Answer answer, String content) {
    answer.setContent(content);
    answerRespository.save(answer);
  }

  public void delete(Answer answer) {
    answerRespository.delete(answer);
  }

  public void vote(Answer answer, Member member) {
    answer.getVoter().add(member);
    answerRespository.save(answer);
  }
}

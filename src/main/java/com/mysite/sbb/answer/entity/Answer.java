package com.mysite.sbb.answer.entity;

import com.mysite.sbb.common.audit.BaseEntity;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Answer extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "answer_id")
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String content;

//  @CreatedDate
//  private LocalDateTime created;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  @ManyToOne(fetch = FetchType.LAZY) // 사용자 1명이 답변을 여러개 작성할 수 있음
  @JoinColumn(name = "member_id", nullable = false)
  private Member author;
}

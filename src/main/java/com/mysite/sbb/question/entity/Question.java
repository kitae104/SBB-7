package com.mysite.sbb.question.entity;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.common.audit.BaseEntity;
import com.mysite.sbb.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Question extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "question_id")
  private Long id;

  @Column(length = 200, nullable = false)
  private String subject;

  @Column(columnDefinition = "TEXT")
  private String content;

//  @CreatedDate
//  private LocalDateTime created;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  private List<Answer> answerList;

  @ManyToOne(fetch = FetchType.LAZY) // 사용자 1명이 질문을 여러개 작성할 수 있음
  @JoinColumn(name = "member_id", nullable = false)
  private Member author;
}

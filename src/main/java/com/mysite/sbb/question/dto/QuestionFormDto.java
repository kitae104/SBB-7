package com.mysite.sbb.question.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionFormDto {

  @NotEmpty(message = "제목은 필수항목입니다.")
  @Size(max = 200, message = "제목은 200자 이내로 입력해주세요.")
  private String subject;

  @NotEmpty(message = "내용은 필수항목입니다.")
  private String content;
}

package com.mysite.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing  // --> auditing 기능 추가후 삭제
public class SbbApplication {

  public static void main(String[] args) {
    SpringApplication.run(SbbApplication.class, args);
  }

}

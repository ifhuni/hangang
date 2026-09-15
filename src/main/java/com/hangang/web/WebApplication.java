package com.hangang.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hangang.web")
public class WebApplication {
  public static void main(String[] args) {
      SpringApplication.run(WebApplication.class, args);
  }
}
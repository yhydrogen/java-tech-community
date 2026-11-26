package com.example.ssmstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SsmStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsmStudyApplication.class, args);
    }

}

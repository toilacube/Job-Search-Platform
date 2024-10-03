package com.training.lehoang;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEmailTools
@EnableScheduling
public class LehoangApplication {

    public static void main(String[] args) {

        SpringApplication.run(LehoangApplication.class, args);
        System.out.println("Hello world");
   }

}

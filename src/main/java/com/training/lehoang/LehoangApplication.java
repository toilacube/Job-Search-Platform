package com.training.lehoang;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEmailTools
public class LehoangApplication {

    public static void main(String[] args) {
        SpringApplication.run(LehoangApplication.class, args);
   }

}

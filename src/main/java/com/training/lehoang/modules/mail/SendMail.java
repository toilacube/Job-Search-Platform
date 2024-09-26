package com.training.lehoang.modules.mail;

import com.google.common.collect.Lists;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class SendMail {
    private final EmailService emailService;

    @GetMapping("")
    public void sendMail()  {
    
    }

  



}

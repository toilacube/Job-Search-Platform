package com.training.lehoang.modules.mail;

import com.google.common.collect.Lists;
import com.training.lehoang.dto.response.JobApplicationResponse;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.Test;
import com.training.lehoang.entity.User;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.TemplateService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final EmailService emailService;
    private final TemplateService templateService;

    @Value("${test.email}")
    private String recruiterEmail;
    
    public void sendEmailWithOutTemplating(UserAndJob user) throws IOException, MessagingException {
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("21520870@gm.uit.edu.vn", "Marco Tullio Cicerone "))
                .to(Lists.newArrayList(new InternetAddress(recruiterEmail, "Helloooooo")))
                .subject("Laelius de amicitia")
                .body("ok")
                .encoding("UTF-8").build();

        MimeMessage m = this.emailService.send(email);
        System.out.println(m.getContent());
    }
    public void sendEmailWithTemplating(UserAndJob userAndJob){
        Arrays.asList(new Cospirator("nguyensylehoang@gmail.com", "There is a new Application, Check this out"))
                .forEach(tyrannicida -> {
                    final Email email;
                    try {
                        email = DefaultEmail.builder()
                                .from(new InternetAddress("151115rubikcube@gmail.com", "Innotech VN"))
                                .to(Lists.newArrayList(new InternetAddress(tyrannicida.getEmail(), tyrannicida.getName())))
                                .subject("Cover letter: ")
                                .body("")//Empty body
                                .encoding("UTF-8").build();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    final Map<String, Object> modelObject = new HashMap<>();
                    modelObject.put("userAndJob", userAndJob);

                    try {
                        emailService.send(email, "test.html", modelObject);
                    } catch (CannotSendEmailException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static class Cospirator {
        private String email;
        private String name;

        public Cospirator(final String email, final String name) {
            this.email = email;
            this.name = name;
        }
        public String getEmail() {
            return email;
        }
        public String getName(){
            return name;
        }
    }
}

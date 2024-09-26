package com.training.lehoang.modules;

import com.google.common.collect.Lists;
import com.training.lehoang.entity.Job;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
       this.sendEmailWithTemplating();
    }

    public void sendEmailWithOutTemplating() throws IOException, MessagingException{
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("21520870@gm.uit.edu.vn", "Marco Tullio Cicerone "))
                .to(Lists.newArrayList(new InternetAddress("nguyensylehoang@gmail.com", "Pomponius Attĭcus")))
                .subject("Laelius de amicitia")
                .body("Firmamentum autem stabilitatis constantiaeque eius, quam in amicitia quaerimus, fides est.")
                .encoding("UTF-8").build();

        MimeMessage m = this.emailService.send(email);
        System.out.println(m.getContent());
    }

    public void sendEmailWithTemplating(){
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
                    Job job = new Job();
                    job.setJobTitle("Software Engineer");
                    job.setCompanyName("Tech Corp");
                    job.setSalary(new BigDecimal(75000));

                    final Map<String, Object> modelObject = new HashMap<>();
                    modelObject.put("application", tyrannicida.getName());
                    modelObject.put("job", job);

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

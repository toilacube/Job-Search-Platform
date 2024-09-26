package com.training.lehoang.modules.rabbitmq;
import com.training.lehoang.dto.response.JobApplicationResponse;
import com.training.lehoang.entity.Test;
import com.training.lehoang.modules.mail.MailService;
import com.training.lehoang.modules.mail.UserAndJob;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JobConsumer {
    private final MailService mailService;
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(UserAndJob u)  {
        this.mailService.sendEmailWithTemplating(u);
    }
}
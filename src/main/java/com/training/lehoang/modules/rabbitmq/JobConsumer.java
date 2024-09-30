package com.training.lehoang.modules.rabbitmq;
import com.training.lehoang.modules.mail.MailService;
import com.training.lehoang.modules.mail.UserAndJob;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobConsumer {
    private final MailService mailService;
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(UserAndJob u)  {
        this.mailService.sendEmailWithTemplating(u);
    }
}
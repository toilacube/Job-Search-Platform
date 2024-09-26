package com.training.lehoang.modules.rabbitmq;

import com.training.lehoang.entity.Test;
import com.training.lehoang.modules.mail.UserAndJob;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange = RabbitMQConfig.EXCHANGE_NAME;

    public void sendApplicationMessage(UserAndJob userAndJob) {
        rabbitTemplate.convertAndSend(exchange, "black", userAndJob);
    }
}
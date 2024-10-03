package com.training.lehoang.modules.rabbitmq;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "spring-boot";
    public static final String EXCHANGE_NAME = "spring-boot-exchange";

    // New Queue
    public static final String JOB_NOTIFICATION_QUEUE_NAME = "job-notification";

    @Bean
    public Queue jobQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue jobNotificationQueue() {
        return new Queue(JOB_NOTIFICATION_QUEUE_NAME, true); // Create the new queue
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue jobQueue, TopicExchange exchange) {
        return BindingBuilder.bind(jobQueue).to(exchange).with("black");
    }

    @Bean
    public Binding jobNotificationBinding(Queue jobNotificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(jobNotificationQueue).to(exchange).with("notification"); // Create binding for the new queue
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

package com.example.grpcservice.config;

import com.example.grpcservice.utils.Constant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue emailNotificationQueue() {
        return new Queue(Constant.EMAIL_NOTIFICATION_QUEUE, true);
    }


    @Bean
    public Queue logQueue() {
        return new Queue(Constant.LOG_QUEUE, true);
    }


}

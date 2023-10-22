package com.example.grpcservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final RabbitTemplate rabbitTemplate;

    public void info(String message) {
        rabbitTemplate.convertAndSend(Constant.LOG_QUEUE, new LogMessage("INFO", message).toJSON());
    }

    public void error(String message) {
        rabbitTemplate.convertAndSend(Constant.LOG_QUEUE, new LogMessage("ERROR", message).toJSON());
    }

}

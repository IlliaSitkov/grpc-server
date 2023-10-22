package com.example.grpcservice.services;

import com.example.grpcservice.models.MessageEntity;
import com.example.grpcservice.models.UserEntity;
import com.example.grpcservice.repositories.MessageRepository;
import com.example.grpcservice.repositories.UserRepository;
import com.example.grpcservice.utils.Constant;
import com.example.grpcservice.utils.EmailNotification;
import com.example.grpcservice.utils.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final RabbitTemplate rabbitTemplate;
    private final LogService logService;

    public MessageEntity saveMessage(int authorId, int receiverId, String message) {
        UserEntity author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("User not found, id = " + authorId));
        UserEntity receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("User not found, id = " + receiverId));
        MessageEntity messageEntity = new MessageEntity(author, receiver, message);
        messageEntity = messageRepository.save(messageEntity);
        rabbitTemplate.convertAndSend(Constant.EMAIL_NOTIFICATION_QUEUE, new EmailNotification(receiver.getEmail(), "You have a new message").toJSON());
        logService.info("Message saved: " + messageEntity);
        return messageEntity;
    }

}

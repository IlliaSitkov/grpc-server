package com.example.grpcservice.controllers;

import com.example.grpcservice.dto.MessagePostDto;
import com.example.grpcservice.services.ChatService;
import com.example.grpcservice.utils.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final LogService logService;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody MessagePostDto message) {
        try {
            chatService.saveMessage(message.getAuthorId(), message.getReceiverId(), message.getMessage());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logService.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}

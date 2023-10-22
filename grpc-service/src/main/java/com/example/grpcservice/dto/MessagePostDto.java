package com.example.grpcservice.dto;

import lombok.Getter;

@Getter
public class MessagePostDto {

    private int authorId;

    private int receiverId;

    private String message;

}

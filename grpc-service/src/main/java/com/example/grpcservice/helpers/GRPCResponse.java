package com.example.grpcservice.helpers;

import com.example.*;
import com.example.grpcservice.models.MessageEntity;
import com.example.grpcservice.models.UserEntity;

public class GRPCResponse {

    public static Status status(boolean success, int code, String message) {
        return Status.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .setCode(code)
                .build();
    }

    public static UserResponse userResponse(Status status, User user) {
        return UserResponse.newBuilder()
                .setStatus(status)
                .setData(user)
                .build();
    }

    public static UserResponse userResponse(Status status) {
        return UserResponse.newBuilder()
                .setStatus(status)
                .build();
    }

    public static Status statusOK() {
        return status(true, 200, "OK");
    }

    public static Status statusBadRequest(String message) {
        return status(false, 400, message);
    }

    public static Status statusInternalServerError(String message) {
        return status(false, 500, message);
    }


    public static User user(int id, String email, String username) {
        return User.newBuilder()
                .setId(id)
                .setEmail(email)
                .setUsername(username)
                .build();
    }

    public static User user(UserEntity user) {
        return user(user.getId(), user.getEmail(), user.getUsername());
    }

    public static StreamMessageResponse streamMessageResponse(Status status, int count) {
        return StreamMessageResponse.newBuilder().setStatus(status).setData(count).build();
    }

    public static Message message(MessageEntity message) {
        return Message.newBuilder()
                .setMessage(message.getMessage())
                .setId(message.getId())
                .setAuthorId(message.getAuthor().getId())
                .setReceiverId(message.getReceiver().getId())
                .build();
    }

    public static MessageResponse messageResponse(Status status, Message message) {
        return MessageResponse.newBuilder()
                .setStatus(status)
                .setData(message)
                .build();
    }

    public static MessageResponse messageResponse(Status status) {
        return MessageResponse.newBuilder()
                .setStatus(status)
                .build();
    }
}

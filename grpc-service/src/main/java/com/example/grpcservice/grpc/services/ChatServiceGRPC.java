package com.example.grpcservice.grpc.services;

import com.example.*;
import com.example.grpcservice.helpers.GRPCResponse;
import com.example.grpcservice.models.MessageEntity;
import com.example.grpcservice.models.UserEntity;
import com.example.grpcservice.repositories.MessageRepository;
import com.example.grpcservice.repositories.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Objects;

@GrpcService
@RequiredArgsConstructor
public class ChatServiceGRPC extends ChatServiceGrpc.ChatServiceImplBase {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    @Override
    public void saveUser(User request, StreamObserver<UserResponse> responseObserver) {
        try {
            UserEntity user = userRepository.save(new UserEntity(request.getEmail(), request.getUsername()));
            responseObserver.onNext(GRPCResponse.userResponse(GRPCResponse.statusOK(), GRPCResponse.user(user)));
        } catch (Exception e) {
            responseObserver.onNext(GRPCResponse.userResponse(GRPCResponse.statusBadRequest(e.getMessage())));
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Message> sendMessage(StreamObserver<StreamMessageResponse> responseObserver) {
        return new StreamObserver<>() {
            private int count;
            private String errorMessage = "";

            @Override
            public void onNext(Message message) {
                try {
                    UserEntity author = userRepository.findById(message.getAuthorId()).orElseThrow(() -> new RuntimeException("User not found, id = " + message.getAuthorId()));
                    UserEntity receiver = userRepository.findById(message.getReceiverId()).orElseThrow(() -> new RuntimeException("User not found, id = " + message.getReceiverId()));
                    MessageEntity messageEntity = new MessageEntity(author, receiver, message.getMessage());
                    messageRepository.save(messageEntity);
                    count++;
                } catch (Exception e) {
                    errorMessage += e.getMessage() + ";";
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onNext(GRPCResponse.streamMessageResponse(GRPCResponse.statusInternalServerError(throwable.getMessage()), count));
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(GRPCResponse.streamMessageResponse(
                        errorMessage.isBlank() ? GRPCResponse.statusOK() : GRPCResponse.statusBadRequest(errorMessage),
                        count
                ));
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getMyUsers(User request, StreamObserver<UserResponse> responseObserver) {
        try {
            UserEntity user = userRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("User not found, id = " + request.getId()));
            userRepository.getUsersByUser(user.getId()).forEach(myUser -> responseObserver.onNext(GRPCResponse.userResponse(GRPCResponse.statusOK(), GRPCResponse.user(myUser))));
        } catch (Exception e) {
            responseObserver.onNext(GRPCResponse.userResponse(GRPCResponse.statusBadRequest(e.getMessage())));
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Message> getMessagesOfUsers(StreamObserver<MessageResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Message message) {
                try {
                    UserEntity author = userRepository.findById(message.getAuthorId()).orElseThrow(() -> new RuntimeException("User not found, id = " + message.getAuthorId()));
                    UserEntity receiver = userRepository.findById(message.getReceiverId()).orElseThrow(() -> new RuntimeException("User not found, id = " + message.getReceiverId()));
                    author.getSentMessages().stream()
                            .filter(m -> Objects.equals(m.getReceiver().getId(), receiver.getId()))
                            .forEach(m -> responseObserver.onNext(GRPCResponse.messageResponse(GRPCResponse.statusOK(), GRPCResponse.message(m))));
                } catch (Exception e) {
                    responseObserver.onNext(GRPCResponse.messageResponse(GRPCResponse.statusBadRequest(e.getMessage())));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onNext(GRPCResponse.messageResponse(GRPCResponse.statusInternalServerError(throwable.getMessage())));
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}

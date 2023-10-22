package com.example.grpcservice.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private UserEntity receiver;

    private String message;

    public MessageEntity(UserEntity author, UserEntity receiver, String message) {
        this.author = author;
        this.receiver = receiver;
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", author=" + author.getId() +
                ", receiver=" + receiver.getId() +
                ", message='" + message + '\'' +
                '}';
    }
}

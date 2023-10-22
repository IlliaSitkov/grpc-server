package com.example.grpcservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private Set<MessageEntity> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private Set<MessageEntity> receivedMessages;

    public UserEntity(String email, String username) {
        this.email = email;
        this.username = username;
    }
}

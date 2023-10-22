package com.example.grpcservice.repositories;

import com.example.grpcservice.models.MessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MessageEntity, Integer> {
}

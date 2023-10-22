package com.example.grpcservice.repositories;

import com.example.grpcservice.models.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {


    @Query(value =
            "SELECT DISTINCT u2 " +
            "FROM UserEntity u1 JOIN MessageEntity m ON u1.id = m.author.id " +
            "JOIN UserEntity u2 ON u2.id = m.receiver.id " +
            "WHERE u1.id = :user_id")
    Iterable<UserEntity> getUsersByUser(@Param("user_id") int userId);


}

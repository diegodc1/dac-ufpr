package com.dac.authentication_service.repository;


import com.dac.authentication_service.collection.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(String login);
}

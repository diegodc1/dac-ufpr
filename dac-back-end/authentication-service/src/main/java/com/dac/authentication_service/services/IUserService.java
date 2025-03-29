package com.dac.authentication_service.services;

import com.dac.authentication_service.collection.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findBylogin(String login);
}

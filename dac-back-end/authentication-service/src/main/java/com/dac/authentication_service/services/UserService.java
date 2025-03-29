package com.dac.authentication_service.services;

import com.dac.authentication_service.collection.User;
import com.dac.authentication_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findBylogin(String login) {
        return userRepository.findByLogin(login);
    }
}

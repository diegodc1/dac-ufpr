package com.dac.authentication_service.securityService;

import com.dac.authentication_service.collection.User;
import com.dac.authentication_service.repository.UserRepository;
import com.dac.authentication_service.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public Boolean saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(String username) {
        return tokenService.generateToken(username);
    }

    public void validateToken(String token) {
        tokenService.validateToken(token);
    }



}

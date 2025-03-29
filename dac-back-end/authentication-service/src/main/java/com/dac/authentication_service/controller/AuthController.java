package com.dac.authentication_service.controller;

import com.dac.authentication_service.collection.User;
import com.dac.authentication_service.dto.AuthRequestDTO;
import com.dac.authentication_service.dto.LoginReturnDTO;
import com.dac.authentication_service.repository.UserRepository;
import com.dac.authentication_service.securityService.AuthService;
import com.dac.authentication_service.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity addNewUser(@RequestBody User user) {
        if (this.userRepository.findByLogin(user.getLogin()).isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe!");

        if (authService.saveUser(user)) {
            return ResponseEntity.ok().body("Usuário cadastrado com sucesso!");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar o novo usuário!") ;
    }

    @PostMapping("/login")
    public ResponseEntity doLogin(@RequestBody AuthRequestDTO authRequestDTO) {

        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(authRequestDTO.getLogin(),
                                    authRequestDTO.getPassword()));

            if (authenticate.isAuthenticated()) {
                String userToken = authService.generateToken(authRequestDTO.getLogin());
                Optional<User> optionalUser = userService.findBylogin(authRequestDTO.getLogin());

                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    LoginReturnDTO response = LoginReturnDTO.builder()
                            .userId(user.getId())
                            .token(userToken)
                            .name(user.getName())
                            .login(user.getLogin())
                            .role(user.getRole())
                            .build();

                    return ResponseEntity.ok(response);
                }
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Erro ao realizar login: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível realizar o login! Tente novamente!");
    }

    @GetMapping("/validate")
    public String validateToken(@RequestHeader("x-access-token") String token) {
        authService.validateToken(token);
        return "O Token é valido!";
    }
}

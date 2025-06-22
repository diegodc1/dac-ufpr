package com.dac.authentication_service.config;


import com.dac.authentication_service.collection.User;
import com.dac.authentication_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (userRepository.findByLogin("func_pre@gmail.com").isEmpty()) {

                User user = User.builder()
                        .name("Funcionário Padrão")
                        .login("func_pre@gmail.com")
                        .password(passwordEncoder.encode("TADS"))
                        .role("FUNCIONARIO")
                        .userStatus("ATIVO")
                        .build();
                userRepository.save(user);

                System.out.println("Usuário padrão inseridos com sucesso!");
            } else {
                System.out.println("Usuário padrão já existe.");
            }
        };
    }
}
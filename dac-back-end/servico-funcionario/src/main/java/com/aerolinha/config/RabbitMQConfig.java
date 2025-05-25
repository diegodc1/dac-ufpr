package com.aerolinha.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder().findAndAddModules().build();
    }


    @Bean
    public Queue canalFuncionario() {
        return new Queue("CanalFuncionario", true);
    }

    @Bean
    public Queue canalFuncionarioRes() {
        return new Queue("CanalFuncionarioRes", true);
    }

    @Bean
    public Queue canalFuncionarioResposta() {
        return new Queue("CanalFuncRes", true);
    }
}

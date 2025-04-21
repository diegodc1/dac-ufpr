package com.aerolinha.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.sagas.criafuncionariosaga.CriaFuncionarioSAGA;
import com.aerolinha.sagas.criafuncionariosaga.eventos.EventoFuncUserCriado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AutListener {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CriaFuncionarioSAGA criaFuncionarioSAGA;

    @RabbitListener(queues = "CanalAutRes")
    public void handleAuthResponses(String mensagem) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(mensagem, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String tipoMensagem = (String) map.get("mensagem");

            switch (tipoMensagem) {

                // R17
                case "EventoFuncUserCriado" -> {

                    EventoFuncUserCriado evento = objectMapper.convertValue(map, EventoFuncUserCriado.class);
                    criaFuncionarioSAGA.manipularUsuarioCriado(evento);
                    break;
                }

            }

        }
    }
}

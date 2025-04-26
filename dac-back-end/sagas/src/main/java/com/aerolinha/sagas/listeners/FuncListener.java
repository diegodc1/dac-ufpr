package com.aerolinha.sagas.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.sagas.criafuncionariosaga.CriaFuncionarioSAGA;
import com.aerolinha.sagas.criafuncionariosaga.eventos.EventoFuncCriado;
import com.aerolinha.sagas.deletarfuncionariosaga.DelFuncSaga;
import com.aerolinha.sagas.deletarfuncionariosaga.eventos.EventoFuncInativado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FuncListener {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CriaFuncionarioSAGA criaFuncionarioSAGA;

    @Autowired
    private DelFuncSaga delFuncSaga;

    @RabbitListener(queues = "CanalFuncRes")
    public void handleAuthResponses(String mensagem) throws JsonMappingException, JsonProcessingException {

        Object object = objectMapper.readValue(mensagem, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String tipoMensagem = (String) map.get("mensagem");

            switch (tipoMensagem) {

                // R17
                case "EventoFuncCriado" -> {

                    EventoFuncCriado evento = objectMapper.convertValue(map, EventoFuncCriado.class);

                    criaFuncionarioSAGA.manipularFuncCriado(evento);

                    break;

                }

                // R19
                case "EventoFuncInativado" -> {

                    EventoFuncInativado evento = objectMapper.convertValue(map, EventoFuncInativado.class);

                    delFuncSaga.manipularFuncInativado(evento);

                    break;

                }

            }

        }
    }
}

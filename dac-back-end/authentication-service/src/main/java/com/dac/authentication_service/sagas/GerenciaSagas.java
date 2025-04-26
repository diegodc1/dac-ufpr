package com.dac.authentication_service.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dac.authentication_service.sagas.comandos.ComandoCriarFuncUser;
import com.dac.authentication_service.sagas.comandos.ComandoDelFunc;
import com.dac.authentication_service.sagas.eventos.EventoFuncUserCriado;
import com.dac.authentication_service.sagas.eventos.EventoFuncUserDeletado;
import com.dac.authentication_service.securityService.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GerenciaSagas {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @RabbitListener(queues = "CanalAut")
    public void gerenciaMensagem(String mensagem) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(mensagem, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String tipoMensagem = (String) map.get("mensagem");

            switch (tipoMensagem) {

                // R17
                case "ComandoCriarFuncUser" -> {

                    ComandoCriarFuncUser comando = objectMapper.convertValue(map, ComandoCriarFuncUser.class);

                    EventoFuncUserCriado evento = authService.novoFuncionario(comando);

                    var msg = objectMapper.writeValueAsString(evento);

                    rabbitTemplate.convertAndSend("CanalAutRes", msg);

                    break;

                }

                // R19
                case "ComandoDelFunc" -> {

                    ComandoDelFunc comando = objectMapper.convertValue(map, ComandoDelFunc.class);

                    EventoFuncUserDeletado evento = authService.removerFuncionario(comando);

                    var resMensagem = objectMapper.writeValueAsString(evento);

                    rabbitTemplate.convertAndSend("CanalAutRes", resMensagem);

                    break;

                }

            }

        }

    }

}

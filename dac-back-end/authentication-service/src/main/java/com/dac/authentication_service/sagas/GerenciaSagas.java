package com.dac.authentication_service.sagas;

import java.util.Map;

import com.dac.authentication_service.sagas.comandos.ComandoCadastroCliente;
import com.dac.authentication_service.sagas.eventos.EventoAutenticacaoCriada;
import com.fasterxml.jackson.core.type.TypeReference;
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
    public void gerenciaMensagem(String mensagem) throws JsonProcessingException {

        if (mensagem.startsWith("\"{")) {
            mensagem = objectMapper.readValue(mensagem, String.class);
        }
        Map<String, Object> map = objectMapper.readValue(mensagem, new TypeReference<>() {
        });
        String tipoMensagem = (String) map.get("mensagem");

        switch (tipoMensagem) {
            case "ComandoCadastroCliente" -> {
                ComandoCadastroCliente comando = objectMapper.convertValue(map, ComandoCadastroCliente.class);
                EventoAutenticacaoCriada evento = authService.cadastrarCliente(comando);
                String resMensagem = objectMapper.writeValueAsString(evento);
                rabbitTemplate.convertAndSend("CanalClienteRes", resMensagem);
            }

            case "ComandoCriarFuncUser" -> {
                ComandoCriarFuncUser comando = objectMapper.convertValue(map, ComandoCriarFuncUser.class);
                EventoFuncUserCriado evento = authService.novoFuncionario(comando);
                String msg = objectMapper.writeValueAsString(evento);
                rabbitTemplate.convertAndSend("CanalAutRes", msg);
            }

            // R19
            case "ComandoDelFunc" -> {
                ComandoDelFunc comando = objectMapper.convertValue(map, ComandoDelFunc.class);
                EventoFuncUserDeletado evento = authService.removerFuncionario(comando);
                String resMensagem = objectMapper.writeValueAsString(evento);
                rabbitTemplate.convertAndSend("CanalAutRes", resMensagem);
            }
        }
    }
}

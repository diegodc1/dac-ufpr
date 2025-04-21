package com.aerolinha.sagas;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.sagas.comandos.ComandoCriarFunc;
import com.aerolinha.sagas.consultas.VerificarFuncionario;
import com.aerolinha.sagas.eventos.EventoFuncCriado;
import com.aerolinha.sagas.resposta.VerificarFuncRes;
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
    private ServicoSagas servicoSagas;

    @RabbitListener(queues = "CanalFuncionario")
    public void gerenciaMensagem(String mensagem) throws JsonProcessingException, JsonMappingException {

        Object object = objectMapper.readValue(mensagem, Object.class);

        if (object instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) object;

            String tipoMensagem = (String) map.get("mensagem");

            switch (tipoMensagem) {

                case "VerificarFuncionario" -> {

                    VerificarFuncionario consulta = objectMapper.convertValue(map, VerificarFuncionario.class);

                    VerificarFuncRes res = servicoSagas.verificarFuncionario(consulta);

                    var msg = objectMapper.writeValueAsString(res);

                    rabbitTemplate.convertAndSend("CanalFuncionarioRes", msg);

                    break;

                }

                // R17 - cria registro funcionário
                case "ComandoCriarFunc" -> {
                    ComandoCriarFunc comando = objectMapper.convertValue(map, ComandoCriarFunc.class);

                    EventoFuncCriado evento = servicoSagas.criarFuncionario(comando);

                    String msg = objectMapper.writeValueAsString(evento);

                    rabbitTemplate.convertAndSend("CanalFuncRes", msg);

                    break;

                }

            }

        }

    }

}

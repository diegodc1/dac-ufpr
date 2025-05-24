package com.dac.client.client_service.sagas;


import java.util.Map;

import com.dac.client.client_service.components.EsperaResposta;
import com.dac.client.client_service.model.Cliente;
import com.dac.client.client_service.sagas.comandos.ComandoCadastroCliente;
import com.dac.client.client_service.sagas.eventos.EventoAutenticacaoCriada;
import com.dac.client.client_service.service.ClienteService;
import com.dac.client.client_service.service.EmailService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GerenciaSagas {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EsperaResposta esperaResposta;

    @RabbitListener(queues = "CanalClienteRes")
    public void gerenciaMensagem(String mensagem) throws JsonMappingException, JsonProcessingException {

        if (mensagem.startsWith("\"{")) {
            mensagem = objectMapper.readValue(mensagem, String.class);
        }
        Map<String, Object> map = objectMapper.readValue(mensagem, new TypeReference<>() {});
        String tipoMensagem = (String) map.get("mensagem");

        switch (tipoMensagem) {
            case "EventoAutenticacaoCriada" -> {
                EventoAutenticacaoCriada evento = objectMapper.convertValue(map, EventoAutenticacaoCriada.class);

                if (evento.isSucesso()) {
                    ComandoCadastroCliente comando = evento.getComando();

                    Cliente cliente = clienteService.salvarCliente(comando);
                    emailService.enviarSenhaPorEmail(evento.getEmail(), evento.getSenha());
                    esperaResposta.resolver(comando.getCpf(), cliente);
                } else {
                    System.out.println("Falha ao criar autenticação: " + evento.getMensagemErro());
                    esperaResposta.erro(evento.getLogin(), evento.getMensagemErro());
                }
                break;
            }
        }

    }
}
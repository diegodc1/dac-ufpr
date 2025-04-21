package com.aerolinha.sagas.criafuncionariosaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.dto.requisicao.NovoFuncDTO;
import com.aerolinha.sagas.criafuncionariosaga.comandos.ComandoCriarFunc;
import com.aerolinha.sagas.criafuncionariosaga.comandos.ComandoCriarFuncUser;
import com.aerolinha.sagas.criafuncionariosaga.eventos.EventoFuncCriado;
import com.aerolinha.sagas.criafuncionariosaga.eventos.EventoFuncUserCriado;
import com.aerolinha.utils.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CriaFuncionarioSAGA {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private NovoFuncDTO novoFuncDTO;

    private String senhaUsuario;

    @Autowired
    private EmailService emailService;

    public void manipularRequisicao(NovoFuncDTO novoFuncDTO) throws JsonProcessingException {

        this.novoFuncDTO = novoFuncDTO;

        ComandoCriarFuncUser comando = ComandoCriarFuncUser.builder()
                .nome(novoFuncDTO.getNome())
                .email(novoFuncDTO.getEmail())
                .mensagem("ComandoCriarFuncUser")
                .build();

        var sendingMessage = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalAut", sendingMessage);
    }

    public void manipularUsuarioCriado(EventoFuncUserCriado evento) throws JsonProcessingException {

        this.senhaUsuario = evento.getSenhaUsuario();

        ComandoCriarFunc comando = ComandoCriarFunc.builder()
                .idUsuario(evento.getIdUsuario())
                .nome(novoFuncDTO.getNome())
                .cpf(novoFuncDTO.getCpf())
                .email(novoFuncDTO.getEmail())
                .numeroTelefone(novoFuncDTO.getNumeroTelefone())
                .mensagem("ComandoCriarFunc")
                .build();

        var sendingMessage = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalFuncionario", sendingMessage);
    }

    public void manipularFuncCriado(EventoFuncCriado evento) {

        String subject = "DAC - Senha Empresa Aérea";

        String message = "Sua senha para acesso ao sistema é: " + this.senhaUsuario + "\nLogin com: "
                + evento.getEmail();

        this.emailService.sendApproveEmail(evento.getEmail(), subject, message);
    }
}

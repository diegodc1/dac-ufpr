package com.aerolinha.sagas.deletarfuncionariosaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.sagas.deletarfuncionariosaga.comandos.ComandoDelFunc;
import com.aerolinha.sagas.deletarfuncionariosaga.comandos.ComandoInativarFunc;
import com.aerolinha.sagas.deletarfuncionariosaga.eventos.EventoFuncInativado;
import com.aerolinha.sagas.deletarfuncionariosaga.eventos.EventoFuncUserDeletado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DelFuncSaga { //orquestrador

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    //1. Primeiro envia mensagem para o serviço de autenticação
    public void manipularRequisicao(String idUsuario) throws JsonProcessingException {

        ComandoDelFunc comando = ComandoDelFunc.builder()
                .idUsuario(idUsuario)
                .mensagem("ComandoDelFunc")
                .build();

        var mensagem = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalAut", mensagem);

    }

    //2. Depois vai para o serviço de funcionário
    public void manipularUsuarioRemovido(EventoFuncUserDeletado evento) throws JsonProcessingException {

        ComandoInativarFunc comando = ComandoInativarFunc.builder()
                .usuarioId(evento.getIdUsuario())
                .estadoUsuario(evento.getEstadoUsuario())
                .mensagem("ComandoInativarFunc")
                .build();

        var mensagem = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalFuncionario", mensagem);

    }

    public void manipularFuncInativado(EventoFuncInativado evento) {
        System.out.println("Dados do funcionário inativado:");
        System.out.println("ID funcionário: " + evento.getIdUsuario());
        System.out.println("Nome: " + evento.getNome());
        System.out.println("Estado:" + evento.getEstadoFuncionario());
    }

}

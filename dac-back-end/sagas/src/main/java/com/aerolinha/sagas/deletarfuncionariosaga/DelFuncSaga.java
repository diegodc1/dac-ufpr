package com.aerolinha.sagas.deletarfuncionariosaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.controlador.ControladorSagas;
import com.aerolinha.sagas.deletarfuncionariosaga.comandos.ComandoDelFunc;
import com.aerolinha.sagas.deletarfuncionariosaga.comandos.ComandoInativarFunc;
import com.aerolinha.sagas.deletarfuncionariosaga.eventos.EventoFuncInativado;
import com.aerolinha.sagas.deletarfuncionariosaga.eventos.EventoFuncUserDeletado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DelFuncSaga { // orquestrador

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ControladorSagas controladorSagas;

    private EventoFuncInativado eventoFuncInativado;

    // 1. Primeiro envia mensagem para o serviço de funcionário
    public void manipularRequisicao(Long idUsuario) throws JsonProcessingException {

        ComandoInativarFunc comando = ComandoInativarFunc.builder()
                .id(idUsuario)
                .mensagem("ComandoInativarFunc")
                .build();

        var mensagem = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalFuncionario", mensagem);

    }

    // 2. Depois vai para o serviço de autenticacao
    public void manipularFuncRemovido(EventoFuncInativado evento) throws JsonProcessingException {

        this.eventoFuncInativado = evento;

        ComandoDelFunc comando = ComandoDelFunc.builder()
                .idUsuario(evento.getUserId())
                .mensagem("ComandoDelFunc")
                .build();

        var mensagem = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalAut", mensagem);

    }

    public void manipularFuncInativado(EventoFuncUserDeletado evento) {
        controladorSagas.completarSagaR19(this.eventoFuncInativado.getCodigo(), this.eventoFuncInativado);
    }

}

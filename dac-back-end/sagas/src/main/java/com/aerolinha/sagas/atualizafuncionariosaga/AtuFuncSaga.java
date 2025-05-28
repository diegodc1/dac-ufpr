package com.aerolinha.sagas.atualizafuncionariosaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.dto.requisicao.PutFuncDTO;
import com.aerolinha.sagas.atualizafuncionariosaga.comandos.ComandoAtuFunc;
import com.aerolinha.sagas.atualizafuncionariosaga.eventos.EventoFuncAtu;
import com.aerolinha.sagas.atualizafuncionariosaga.eventos.EventoUsuAtu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AtuFuncSaga {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String idUsuario;

    public void manipularRequisicao(PutFuncDTO dto) throws JsonProcessingException {

        this.idUsuario = dto.getIdUsuario();

        ComandoAtuFunc comando = new ComandoAtuFunc(dto);

        // 1. vai para o serviço Funcionário e Atualiza

        comando.setMensagem("ComandoAtuFunc");

        var sendingMessage = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalFuncionario", sendingMessage);

    }

    public void manipularFuncionarioAtualizado(EventoFuncAtu evento) throws JsonProcessingException {

        if (evento.getProsseguirSAGA()) {

            ComandoAtuFunc comando = ComandoAtuFunc.builder()
                    .idUsuario(this.idUsuario)
                    .nome(evento.getNome())
                    .email(evento.getEmail())
                    .mensagem("ComandoAtuFunc")
                    .build();

            var sendingMessage = objectMapper.writeValueAsString(comando);

            rabbitTemplate.convertAndSend("CanalAut", sendingMessage);

        } else {
            System.out.println("SAGA atualizar funcionário completada para o ID: " + this.idUsuario);
        }
    }

    public void manipularUsuarioAtualizado(EventoUsuAtu evento) {
        System.out.println("SAGA atualizar funcionário completada para o ID: " + evento.getIdUsuario());
    }

}

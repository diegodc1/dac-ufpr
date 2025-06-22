package com.aerolinha.sagas.atualizafuncionariosaga;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aerolinha.controlador.ControladorSagas;
import com.aerolinha.dto.requisicao.PutFuncDTO;
import com.aerolinha.dto.resposta.R18ResDTO;
import com.aerolinha.sagas.atualizafuncionariosaga.comandos.ComandoAtuFunc;
import com.aerolinha.sagas.atualizafuncionariosaga.comandos.ComandoAtuUsu;
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

    @Autowired
    private ControladorSagas controladorSagas;

    private Long codigo;

    private String cpf;

    private String telefone;

    private String senha;

    public void manipularRequisicao(PutFuncDTO dto) throws JsonProcessingException {

        this.senha = dto.getSenha();

        ComandoAtuFunc comando = new ComandoAtuFunc(dto);

        // 1. vai para o serviço Funcionário e Atualiza

        comando.setMensagem("ComandoAtuFunc");

        var sendingMessage = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalFuncionario", sendingMessage);

    }

    public void manipularFuncionarioAtualizado(EventoFuncAtu evento) throws JsonProcessingException {

        this.codigo = evento.getCodigo();
        this.cpf = evento.getCpf();
        this.telefone = evento.getTelefone();

        ComandoAtuUsu comando = ComandoAtuUsu.builder()
                .idUsuario(evento.getIdUsuario())
                .nome(evento.getNome())
                .email(evento.getEmail())
                .senha(this.senha)
                .mensagem("ComandoAtuUsu")
                .build();

        var sendingMessage = objectMapper.writeValueAsString(comando);

        rabbitTemplate.convertAndSend("CanalAut", sendingMessage);

    }

    public void manipularUsuarioAtualizado(EventoUsuAtu evento) {

        R18ResDTO resposta = R18ResDTO.builder()
                .codigo(this.codigo)
                .cpf(this.cpf)
                .email(evento.getEmail())
                .nome(evento.getNome())
                .telefone(this.telefone)
                .build();

        controladorSagas.completarSagaR18(codigo, resposta);

    }

}

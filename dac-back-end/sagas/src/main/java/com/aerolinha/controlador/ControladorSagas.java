package com.aerolinha.controlador;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerolinha.dto.requisicao.NovoFuncDTO;
import com.aerolinha.dto.resposta.R17ResDTO;
import com.aerolinha.dto.resposta.ResGenDTO;
import com.aerolinha.sagas.criafuncionariosaga.CriaFuncionarioSAGA;
import com.aerolinha.sagas.criafuncionariosaga.eventos.EventoFuncCriado;
import com.aerolinha.sagas.criafuncionariosaga.requisicoes.VerificarFuncionario;
import com.aerolinha.sagas.criafuncionariosaga.respostas.VerificarFuncRes;
import com.aerolinha.sagas.deletarfuncionariosaga.DelFuncSaga;
import com.aerolinha.utils.ValidadorCPF;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("saga")
public class ControladorSagas {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ValidadorCPF validadorCPF;

    @Lazy
    @Autowired
    private CriaFuncionarioSAGA criaFuncionarioSAGA;

    @Autowired
    private DelFuncSaga delFuncSaga;

    // Armazenamento temporário para respostas
    private final Map<String, VerificarFuncRes> respostasTemporarias = new ConcurrentHashMap<>();

    private final Map<String, CompletableFuture<EventoFuncCriado>> futuresSaga = new ConcurrentHashMap<>();

    // R17
    @PostMapping("/novo-funcionario")
    public ResponseEntity<?> registrarFunc(@RequestBody NovoFuncDTO novoFuncDTO)
            throws JsonProcessingException, InterruptedException, ExecutionException {

        // 1. Validação do CPF
        if (!validadorCPF.cpfValido(novoFuncDTO.getCpf())) {
            return ResponseEntity.badRequest().body(new ResGenDTO("CPF inválido!"));
        }

        // 2. Preparação da verificação inicial (mantido igual)
        VerificarFuncionario consulta = VerificarFuncionario.builder()
                .cpf(novoFuncDTO.getCpf())
                .email(novoFuncDTO.getEmail())
                .mensagem("VerificarFuncionario")
                .build();

        String chaveResposta = novoFuncDTO.getCpf();

        // 3. Cria o Future para acompanhar a SAGA
        CompletableFuture<EventoFuncCriado> future = new CompletableFuture<>();
        futuresSaga.put(chaveResposta, future);

        // 4. Envia a verificação inicial (mantido igual)
        rabbitTemplate.convertAndSend("CanalFuncionario", objectMapper.writeValueAsString(consulta));

        // 5. Polling para verificação inicial (mantido igual)
        int tentativas = 0;
        while (tentativas < 10 && !respostasTemporarias.containsKey(chaveResposta)) {
            Thread.sleep(500);
            tentativas++;
        }

        // 6. Tratamento da resposta da verificação (modificado apenas o sucesso)
        if (!respostasTemporarias.containsKey(chaveResposta)) {
            futuresSaga.remove(chaveResposta);
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body(new ResGenDTO("Tempo excedido ao verificar funcionário"));
        }

        VerificarFuncRes resposta = respostasTemporarias.remove(chaveResposta);

        if (!resposta.getComecaSaga()) {
            futuresSaga.remove(chaveResposta);
            return ResponseEntity.badRequest().body(new ResGenDTO(resposta.getMensagem()));
        }

        // 7. Inicia a SAGA (mantido igual)
        this.criaFuncionarioSAGA.manipularRequisicao(novoFuncDTO);

        // 8. Aguarda e retorna o resultado da SAGA (modificado)
        try {
            EventoFuncCriado resultado = future.get(30, TimeUnit.SECONDS);
            R17ResDTO answer = new R17ResDTO(resultado);
            return ResponseEntity.status(HttpStatus.CREATED).body(answer);
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body(new ResGenDTO("Tempo excedido ao criar funcionário"));
        } finally {
            futuresSaga.remove(chaveResposta);
        }
    }

    public void completarSaga(String cpf, EventoFuncCriado evento) {
        CompletableFuture<EventoFuncCriado> future = futuresSaga.get(cpf);
        if (future != null) {
            future.complete(evento);
            futuresSaga.remove(cpf);
        }
    }

    // Listener único para receber as respostas da verificação de funcionário
    // pode tambem ser incoporporado mais tarde ao listener do funcionário
    @RabbitListener(queues = "CanalFuncionarioRes")
    public void receberResposta(String mensagem) throws JsonProcessingException {
        VerificarFuncRes resposta = objectMapper.readValue(mensagem, VerificarFuncRes.class);

        // CPF da resposta para usar como chave
        String chave = resposta.getCpf();
        respostasTemporarias.put(chave, resposta);
    }

    // R19
    @DeleteMapping("remover/{id}")
    public ResponseEntity<ResGenDTO> removerFuncionario(@PathVariable("id") String idUsuario)
            throws JsonProcessingException {

        this.delFuncSaga.manipularRequisicao(idUsuario);
        ResGenDTO dto = new ResGenDTO("Funcionário com id " + idUsuario + " foi removido");

        return ResponseEntity.ok().body(dto);
    }

}

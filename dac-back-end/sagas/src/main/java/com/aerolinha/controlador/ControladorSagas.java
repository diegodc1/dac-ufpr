package com.aerolinha.controlador;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerolinha.dto.requisicao.NovoFuncDTO;
import com.aerolinha.dto.resposta.ResGenDTO;
import com.aerolinha.sagas.criafuncionariosaga.CriaFuncionarioSAGA;
import com.aerolinha.sagas.criafuncionariosaga.requisicoes.VerificarFuncionario;
import com.aerolinha.sagas.criafuncionariosaga.respostas.VerificarFuncRes;
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

    @Autowired
    private CriaFuncionarioSAGA criaFuncionarioSAGA;

    // Armazenamento temporário para respostas
    private final Map<String, VerificarFuncRes> respostasTemporarias = new ConcurrentHashMap<>();

    // R17
    @PostMapping("/novo-funcionario")
    public ResponseEntity<ResGenDTO> registrarFunc(@RequestBody NovoFuncDTO novoFuncDTO)
            throws JsonProcessingException, InterruptedException {

        if (!validadorCPF.cpfValido(novoFuncDTO.getCpf())) {
            ResGenDTO dto = new ResGenDTO("CPF inválido!");
            return ResponseEntity.badRequest().body(dto);
        }

        VerificarFuncionario consulta = VerificarFuncionario.builder()
                .cpf(novoFuncDTO.getCpf())
                .email(novoFuncDTO.getEmail())
                .mensagem("VerificarFuncionario")
                .build();

        var mensagem = objectMapper.writeValueAsString(consulta);

        // Usar CPF como chave única para identificar a resposta
        String chaveResposta = novoFuncDTO.getCpf();

        // 1. primeiro verificar se os cpf e email enviados na requsição não existem no
        // serviço de Funcionário
        // caso os dois ou um dos dois existam no serviço de Funcionário retorna
        // resposta ao frontend
        // caso nenhum dos dois exista no serviço de Funcionário começa SAGA de criação
        // de Funcionário novo
        rabbitTemplate.convertAndSend("CanalFuncionario", mensagem);

        // Aguardar a resposta por um tempo limitado
        int tentativas = 0;
        int maxTentativas = 10; // 10 tentativas com 500ms = 5 segundos no total
        while (tentativas < maxTentativas && !respostasTemporarias.containsKey(chaveResposta)) {
            Thread.sleep(500); // Espera 5 seg entre tentativas
            tentativas++;
        }

        if (respostasTemporarias.containsKey(chaveResposta)) {

            VerificarFuncRes resposta = respostasTemporarias.remove(chaveResposta);

            if (resposta.getComecaSaga()) {
                this.criaFuncionarioSAGA.manipularRequisicao(novoFuncDTO);
                return ResponseEntity.ok(new ResGenDTO(resposta.getMensagem()));
            } else {
                return ResponseEntity.badRequest().body(new ResGenDTO(resposta.getMensagem()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body(new ResGenDTO("Tempo excedido ao verificar funcionário"));
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

}

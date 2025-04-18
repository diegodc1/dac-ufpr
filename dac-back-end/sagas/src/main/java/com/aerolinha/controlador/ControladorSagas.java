package com.aerolinha.controlador;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerolinha.dto.requisicao.NovoFuncDTO;
import com.aerolinha.dto.resposta.ResGenDTO;
import com.aerolinha.sagas.requisicoes.VerificarFuncionario;
import com.aerolinha.utils.ValidadorCPF;
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

    // R17
    @PostMapping("/novo-funcionario")
    public ResponseEntity<ResGenDTO> registrarFunc(@RequestBody NovoFuncDTO novoFuncDTO) {

        if (!validadorCPF.cpfValido(novoFuncDTO.getCpf())) {
            ResGenDTO dto = new ResGenDTO("CPF inválido!");
            return ResponseEntity.badRequest().body(dto);
        } else {

            ResGenDTO dto = new ResGenDTO("CPF ok!");
            return ResponseEntity.ok(dto);
        }

        // VerificarFuncionario consulta = VerificarFuncionario.builder()
        // .cpf(novoFuncDTO.getCpf())
        // .email(novoFuncDTO.getEmail())
        // .mensagem("VerificarCliente")
        // .build();

        // return null;
    }

}
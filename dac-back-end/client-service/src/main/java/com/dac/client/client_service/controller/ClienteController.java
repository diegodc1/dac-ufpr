package com.dac.client.client_service.controller;

import com.dac.client.client_service.dto.CadastroClienteDTO;
import com.dac.client.client_service.dto.ClienteRequestDTO;
import com.dac.client.client_service.dto.ClienteResponseDTO;
import com.dac.client.client_service.dto.EnderecoViaCepDTO;
import com.dac.client.client_service.model.Cliente;
import com.dac.client.client_service.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarCliente(@RequestBody CadastroClienteDTO cadastroDTO) {
        try {
            clienteService.iniciarCadastroCliente(cadastroDTO);
            return ResponseEntity.ok().body("Cadastro em processamento! Aguarde o email com sua senha.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }


    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listarTodos().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ClienteResponseDTO salvar(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = toEntity(clienteRequestDTO);
        Cliente clienteSalvo = clienteService.salvar(cliente);
        return toResponseDTO(clienteSalvo);
    }

    @DeleteMapping("/{cpf}")
    public void deletar(@PathVariable String cpf) {
        clienteService.deletar(cpf);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                Long.valueOf(cliente.getCpf()),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getNome(),
                cliente.getSaldoMilhas()
        );
    }

    private Cliente toEntity(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = new Cliente();
        cliente.setCpf(clienteRequestDTO.getCpf());
        cliente.setEmail(clienteRequestDTO.getEmail());
        cliente.setNome(clienteRequestDTO.getNome());
        return cliente;
    }
}
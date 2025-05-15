package com.dac.backend.clientservice.controller;

import com.dac.backend.clientservice.dto.ClienteRequestDTO;
import com.dac.backend.clientservice.dto.ClienteResponseDTO;
import com.dac.backend.clientservice.model.Cliente;
import com.dac.backend.clientservice.service.ClienteService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
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
    
    @GetMapping("/{codigoCliente}/milhas")
    public ResponseEntity<Map<String, Object>> getMilhasCliente(@PathVariable Long codigoCliente) {
        Cliente cliente = clienteService.buscarPorCodigo(codigoCliente);
        
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Map<String, Object>> transacoes = clienteService.buscarTransacoesPorCliente(codigoCliente);
        
        Map<String, Object> response = new HashMap<>();
        response.put("codigo", cliente.getCodigo());
        response.put("saldo_milhas", cliente.getSaldoMilhas());
        response.put("transacoes", transacoes);
        
        return ResponseEntity.ok(response);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getCodigo(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getSaldoMilhas()
        );
    }

    private Cliente toEntity(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = new Cliente();
        cliente.setCpf(clienteRequestDTO.getCpf());
        cliente.setEmail(clienteRequestDTO.getEmail());
        cliente.setNome(clienteRequestDTO.getNome());
        cliente.setTelefone(clienteRequestDTO.getTelefone());
        cliente.setSenha(clienteRequestDTO.getSenha());
        return cliente;
    }
}
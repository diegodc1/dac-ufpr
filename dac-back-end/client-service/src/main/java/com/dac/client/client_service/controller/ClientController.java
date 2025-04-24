package com.dac.backend.clientservice.controller;

import com.dac.backend.clientservice.dto.ClienteRequestDTO;
import com.dac.backend.clientservice.dto.ClienteResponseDTO;
import com.dac.backend.clientservice.model.Cliente;
import com.dac.backend.clientservice.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
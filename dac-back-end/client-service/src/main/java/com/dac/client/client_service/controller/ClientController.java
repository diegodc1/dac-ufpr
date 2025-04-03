package com.dac.backend.clientservice.controller;

import com.dac.backend.clientservice.model.Cliente;
import com.dac.backend.clientservice.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        return clienteService.salvar(cliente);
    }

    @DeleteMapping("/{cpf}")
    public void deletar(@PathVariable String cpf) {
        clienteService.deletar(cpf);
    }
}
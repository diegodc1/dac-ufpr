package com.dac.backend.funcionarioservice.controller;

import com.dac.backend.funcionarioservice.model.Funcionario;
import com.dac.backend.funcionarioservice.service.FuncionarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public List<Funcionario> listar() {
        return funcionarioService.listarTodos();
    }

    @PostMapping
    public Funcionario salvar(@RequestBody Funcionario funcionario) {
        return funcionarioService.salvar(funcionario);
    }

    @DeleteMapping("/{cpf}")
    public void deletar(@PathVariable String cpf) {
        funcionarioService.deletar(cpf);
    }
}
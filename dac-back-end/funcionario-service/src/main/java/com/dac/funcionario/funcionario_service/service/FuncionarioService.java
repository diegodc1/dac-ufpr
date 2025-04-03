package com.dac.backend.funcionarioservice.service;

import com.dac.backend.funcionarioservice.model.Funcionario;
import com.dac.backend.funcionarioservice.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Funcionario salvar(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void deletar(String cpf) {
        funcionarioRepository.deleteById(cpf);
    }
}
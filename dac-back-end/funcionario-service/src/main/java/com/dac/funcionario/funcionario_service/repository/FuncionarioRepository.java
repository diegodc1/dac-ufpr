package com.dac.backend.funcionarioservice.repository;

import com.dac.backend.funcionarioservice.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
}
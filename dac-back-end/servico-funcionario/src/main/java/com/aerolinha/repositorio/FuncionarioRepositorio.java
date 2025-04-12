package com.aerolinha.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerolinha.modelo.Funcionario;

@Repository
public interface FuncionarioRepositorio extends JpaRepository<Funcionario,UUID>{
    
}

package com.aerolinha.repositorio;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aerolinha.modelo.Funcionario;

@Repository
public interface FuncionarioRepositorio extends JpaRepository<Funcionario, UUID> {
    Funcionario findByCpfAndEmail(String cpf, String email);

    @Query(value = "SELECT * FROM tabela_funcionario f WHERE f.cpf = ?1", nativeQuery = true)
    Funcionario getFuncionarioByCpf(String cpf);

    @Query(value = "SELECT * FROM tabela_funcionario f WHERE f.email = ?1", nativeQuery = true)
    Funcionario getFuncionarioByEmail(String email);

}

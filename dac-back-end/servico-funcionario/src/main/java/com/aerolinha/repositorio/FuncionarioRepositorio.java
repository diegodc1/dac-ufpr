package com.aerolinha.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aerolinha.modelo.Funcionario;

@Repository
public interface FuncionarioRepositorio extends JpaRepository<Funcionario, Long> {
    Funcionario findByCpfAndEmail(String cpf, String email);

    @Query(value = "SELECT * FROM tabela_funcionario f WHERE f.cpf = ?1", nativeQuery = true)
    Funcionario getFuncionarioByCpf(String cpf);

    @Query(value = "SELECT * FROM tabela_funcionario f WHERE f.email = ?1", nativeQuery = true)
    Funcionario getFuncionarioByEmail(String email);

    // R16
    @Query(value = "SELECT * FROM tabela_funcionario f WHERE f.estado = 'ATIVO' ORDER BY f.nome", nativeQuery = true)
    List<Funcionario> getFuncionariosAtivos();

    Funcionario findByIdUsuario(String idUsuario);

    Funcionario findByIdFuncionario(Long idFuncionario);

    Funcionario findByEmail(String email);
}

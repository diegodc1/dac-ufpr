package com.aerolinha.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tabela_funcionario")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario", nullable = false, unique = true, updatable = false)
    private Long idFuncionario;
    @Column(name = "id_usuario", nullable = false, unique = true, updatable = false)
    private String idUsuario;
    @Column(name = "nome", nullable = false, unique = false, updatable = true)
    private String nome;
    @Column(name = "cpf", nullable = false, unique = true, updatable = false)
    private String cpf;
    @Column(name = "email", nullable = false, unique = true, updatable = true)
    private String email;
    @Column(name = "numero_telefone", nullable = false, unique = false, updatable = true)
    private String numeroTelefone;
    @Column(name = "estado", nullable = false, updatable = true)
    private String estado;
}

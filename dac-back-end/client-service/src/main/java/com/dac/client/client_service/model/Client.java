package com.dac.backend.clientservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String ruaNumero;
    private String complemento;
    private String cep;
    private String cidade;
    private String uf;

    @Column(nullable = false)
    private int saldoMilhas;
}
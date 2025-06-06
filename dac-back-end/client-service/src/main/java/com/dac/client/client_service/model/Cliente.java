package com.dac.client.client_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private String rua;
    private String numero;
    private String complemento;
    private String cep;
    private String cidade;
    private String uf;
    private String bairro;

    @Column(nullable = false)
    private int saldoMilhas;


}
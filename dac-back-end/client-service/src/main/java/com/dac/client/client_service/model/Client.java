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
    private String cpf;

    private String nome;
    private String email;
    private String ruaNumero;
    private String complemento;
    private String cep;
    private String cidade;
    private String uf;
    private int milhas;
}
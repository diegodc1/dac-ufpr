package com.dac.backend.funcionarioservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {

    @Id
    private String cpf;

    private String nome;
    private String email;
    private String telefone;
}
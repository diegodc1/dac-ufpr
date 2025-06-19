package com.example.reservas.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AeroportoModel {

    private String codigo;
    private String nome;
    private String cidade;
    private String uf;
}

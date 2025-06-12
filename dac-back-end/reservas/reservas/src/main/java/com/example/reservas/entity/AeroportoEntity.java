package com.example.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_aeroporto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AeroportoEntity implements Serializable {

    @Id
    @Column(name = "codigo", length = 10, nullable = false, updatable = false)
    private String codigo;  // Código IATA do aeroporto, exemplo: "GRU", "CWB"

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "uf", length = 2, nullable = false)
    private String uf;  // Estado abreviado, ex: "SP", "RJ"
}

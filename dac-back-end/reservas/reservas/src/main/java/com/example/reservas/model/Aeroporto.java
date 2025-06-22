package com.example.reservas.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_aeroporto")
public class Aeroporto implements Serializable {

    @Id
    @Column(length = 3)
    private  String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String cidade;

    @Column(nullable = false, length = 20)
    private String uf;
}

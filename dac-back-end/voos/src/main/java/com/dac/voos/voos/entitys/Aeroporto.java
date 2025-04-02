package com.dac.voos.voos.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table (name = "aeroporto")
public class Aeroporto {
    @Id
    @Column(length = 3, nullable = false, name = "cod_aero")
    private  String codigo;

    @Column(name = "nome_aero", nullable = false)
    private  String nome;

    @Column(name = "cidade", nullable = false)
    private  String cidade;

    @Column(name = "uf", nullable = false)
    private  String uf;
}

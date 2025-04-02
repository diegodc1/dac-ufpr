package com.dac.voos.voos.entitys;
import jakarta.persistence.*;
        import lombok.*;

@Data
@Entity
@Table(name = "estadoVoo")

public class EstadoVoo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sigla_estado", nullable = false, unique = true)
    private String sigla;

    @Column(name = "descricao", nullable = false, unique = true)
    private String descricao;


    public EstadoVoo() {

    }
}

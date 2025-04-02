package com.dac.voos.voos.entitys;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "voos")
public class Voos {

    @Id
    @Column(name = "cod_voo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime data_hora;

    @ManyToOne
    @JoinColumn(name = "cod_aero_origem")
    private  Aeroporto aeroportoOrigem;

    @ManyToOne
    @JoinColumn(name = "cod_aero_destino")
    private Aeroporto aeroportoDestino;

    @Column(name = "valor_Passagem", nullable = false)
    private BigDecimal valorPassagem;

    @Column(name = "qtd_poltrona_total",nullable = false)
    private int quantidadePoltronasTotal;

    @Column(name = "qtd_poltrona_oculpada", nullable = false)
    private int quantidadePoltronasOculpadas;

    @ManyToOne
    @JoinColumn(name = "estado_voo_id")
    private EstadoVoo estadoVoo;




    public void setAeroportoOrigem(Aeroporto aeroportoOrigem) {
        this.aeroportoOrigem = aeroportoOrigem;
    }

    public void setAeroportoDestino(Aeroporto aeroportoDestino) {
        this.aeroportoDestino = aeroportoDestino;
    }

    public void setEstadoVoo(EstadoVoo estadoVoo) {
        this.estadoVoo = estadoVoo;
    }

}

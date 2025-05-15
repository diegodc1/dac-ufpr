package com.example.reservas.model;



import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_reserva")
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reserva", nullable = false, updatable = false)
    private UUID idReserva;

    @Column(name = "cod_reserva", nullable = false, unique = true, updatable = false)
    private String codReserva;

    @Column(name = "cod_voo", nullable = false)
    private String codVoo;

    @Column(name = "data_reserva", nullable = false)
    private ZonedDateTime dataReserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_reserva", referencedColumnName = "id_status", nullable = false)
    private StatusReserva statusReserva;

    @Column(name = "valor_gasto", nullable = false)
    private BigDecimal valorGasto;

    @Column(name = "milhas_gastadas", nullable = false)
    private Integer milhasGastadas;

    @Column(name = "num_assento", nullable = false)
    private Integer numeroAssento;

    @Column(name = "id_usuario", nullable = false)
    private String idUsuario;

    @Column(name = "id_transacao", nullable = false, unique = true)
    private UUID idTransacao;
}

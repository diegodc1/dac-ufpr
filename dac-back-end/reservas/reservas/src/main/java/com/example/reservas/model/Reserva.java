package com.example.reservas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

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
    private String codigoReserva;

    @Column(name = "codigo_voo", nullable = false)
    private String codigoVoo;

    @Column(name = "data_reserva", nullable = false)
    private ZonedDateTime dataReserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_reserva", referencedColumnName = "id_estado", nullable = false)
    private StatusReserva estadoReserva;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "milhas_utilizadas", nullable = false)
    private Integer milhasUtilizadas;

    @Column(name = "quantidade_poltronas", nullable = false)
    private Integer quantidadePoltronas;

    @Column(name = "codigo_cliente", nullable = false)
    private String codigoCliente;

    @Column(name = "id_transacao", nullable = false, unique = true)
    private UUID idTransacao;

    @Column(name = "cod_aeroporto_origem", nullable = false)
    private String codigoAeroportoOrigem;

    @Column(name = "cod_aeroporto_destino", nullable = false)
    private String codigoAeroportoDestino;
}

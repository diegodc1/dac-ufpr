package com.example.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reserva", nullable = false, updatable = false)
    private UUID idReserva;

    @Column(name = "codigo", nullable = false, unique = true, updatable = false)
    private String codigo;

    @Column(name = "codigo_voo", nullable = false)
    private String codigoVoo;

    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "estado", referencedColumnName = "id_estado", nullable = false)
private EstadoReservaEntity descricao;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "milhas_utilizadas", nullable = false)
    private Integer milhasUtilizadas;

    @Column(name = "quantidade_poltronas", nullable = false)
    private Integer quantidadePoltronas;

    @Column(name = "codigo_cliente", nullable = false)
    private Integer codigoCliente;

    @Column(name = "id_transacao", nullable = false, unique = true)
    private UUID idTransacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aeroporto_origem", referencedColumnName = "codigo", nullable = false)
    private AeroportoEntity aeroportoOrigem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aeroporto_destino", referencedColumnName = "codigo", nullable = false)
    private AeroportoEntity aeroportoDestino;
}

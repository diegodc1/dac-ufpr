package com.example.reservas.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

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

    @Column(name = "codigo", nullable = false, unique = true, updatable = false)
    private String codigo;

    @Column(name = "codigo_voo", nullable = false)
    private String codigoVoo;

    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado", referencedColumnName = "id_estado", nullable = false)
    private EstadoReserva estado;

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
    private Aeroporto aeroportoOrigem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aeroporto_destino", referencedColumnName = "codigo", nullable = false)
    private Aeroporto aeroportoDestino;

    // Método que será executado antes de a reserva ser persistida no banco de dados
    @PrePersist
    public void prePersist() {
        // Se a data não for informada, define como a data atual
        if (this.data == null) {
            this.data = ZonedDateTime.now();
        }

        // Se o estado não for informado, define o estado como "CONFIRMADA"
        if (this.estado == null) {
            EstadoReserva estadoDefault = new EstadoReserva();
            estadoDefault.setDescricaoEstado("CONFIRMADA");  // Aqui, definimos o estado para "CONFIRMADA"
            estadoDefault.setAcronimoEstado("CONF");  // Código do estado, por exemplo: "CONF"
            this.estado = estadoDefault;
        }

        // Se o idTransacao não for informado, gera um novo UUID
        if (this.idTransacao == null) {
            this.idTransacao = UUID.randomUUID();
        }
    }
}

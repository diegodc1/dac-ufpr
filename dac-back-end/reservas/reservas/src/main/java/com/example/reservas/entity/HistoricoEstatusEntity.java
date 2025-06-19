package com.example.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
@Entity
@Table(name = "tb_historico_estatus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoEstatusEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_historico", nullable = false, updatable = false)
    private UUID idHistorico;

    @Column(name = "data_alt_estado", nullable = false)
    private ZonedDateTime dataAltEstado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST) // Cascade aqui
    @JoinColumn(name = "reserva", referencedColumnName = "id_reserva", nullable = false)
    private ReservaEntity reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST) // Cascade aqui
    @JoinColumn(name = "estado_inicial", referencedColumnName = "id_estado", nullable = false)
    private EstadoReservaEntity estadoInicial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST) // Cascade aqui
    @JoinColumn(name = "estado_final", referencedColumnName = "id_estado", nullable = false)
    private EstadoReservaEntity estadoFinal;

    // Construtor com parâmetros
    public HistoricoEstatusEntity(ReservaEntity reserva, EstadoReservaEntity estadoInicial, EstadoReservaEntity estadoFinal) {
        this.reserva = reserva;
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.dataAltEstado = ZonedDateTime.now(); // Define automaticamente a data de alteração
    }
}

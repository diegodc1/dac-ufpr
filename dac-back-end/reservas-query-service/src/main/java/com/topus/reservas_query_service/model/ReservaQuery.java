package com.topus.reservas_query_service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.topus.reservas_query_service.sagas.command.ReservaCommand;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tb_reserva_query")
public class ReservaQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reserva", nullable = false, updatable = false)
    private UUID idReserva;

    @Column(name = "id_reserva_command", nullable = false)
    private UUID idReservaCommand; // * id in booking command service

    @Column(name = "cod_reserva", nullable = false)
    private String codReserva;

    @Column(name = "cod_voo", nullable = false)
    private String codVoo;

    @Column(name = "data_reserva", nullable = false)
    private ZonedDateTime dataReserva;

    // booking status
    @Column(name = "id_status_command", nullable = false)
    private UUID idStatusCommand; // * id in booking command service

    @Column(name = "cod_status", nullable = false)
    private Integer codStatus;

    @Column(name = "status_acronym", nullable = false)
    private String statusAcronym;

    @Column(name = "status_descricao", nullable = false)
    private String statusDescricao;
    // *******************************************************

    @Column(name = "valor_gasto", nullable = false)
    private BigDecimal valorGasto;

    @Column(name = "milhas_gastadas", nullable = false)
    private Integer milhasGastadas;

    @Column(name = "numero_assento", nullable = false)
    private Integer numeroAssento;

    @Column(name = "id_usuario", nullable = false)
    private String idUsuario;

    @Column(name = "id_transacao", nullable = false)
    private UUID idTransacao;

    // constructor
    public ReservaQuery(ReservaCommand reservaCommand) {

        this.idReservaCommand = reservaCommand.getIdReservaCommand();
        this.codReserva = reservaCommand.getCodReserva();
        this.codVoo = reservaCommand.getCodVoo();
        this.dataReserva = reservaCommand.getDataReserva();
        this.idStatusCommand = reservaCommand.getIdStatusCommand();
        this.codStatus = reservaCommand.getCodStatus();
        this.statusAcronym = reservaCommand.getStatusAcronym();
        this.statusDescricao = reservaCommand.getStatusDescricao();
        this.valorGasto = reservaCommand.getValorGasto();
        this.milhasGastadas = reservaCommand.getMilhasGastadas();
        this.numeroAssento = reservaCommand.getNumeroAssento();
        this.idUsuario= reservaCommand.getIdUsuario();
        this.idTransacao = reservaCommand.getIdTransacao();
    }
}

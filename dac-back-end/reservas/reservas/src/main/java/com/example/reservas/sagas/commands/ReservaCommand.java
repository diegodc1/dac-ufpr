package com.example.reservas.sagas.commands;




import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.example.reservas.model.Reserva;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaCommand {
    private UUID idReservaCommand;
    private String codReserva;
    private String codVoo;
    private ZonedDateTime dataReserva;

    //status da reserva
    private UUID idStatusCommand;
    private Integer codStatus;
    private String acronimoStatus;
    private String descricaoStatus;

    // *****
    private BigDecimal valorGasto;
    private Integer milhasGastadas;
    private Integer numeroAssento;
    private String idUsuario;
    private UUID idTransacao;
    private String messageType;

    public ReservaCommand(Reserva reserva) {
        idReservaCommand = reserva.getIdReserva();
        codReserva = reserva.getCodReserva();
        codVoo = reserva.getCodVoo();
        dataReserva = reserva.getDataReserva();
        idStatusCommand= reserva.getStatusReserva().getIdStatus();
        codStatus = reserva.getStatusReserva().getCodStatus();
        acronimoStatus = reserva.getStatusReserva().getAcronimoStatus();
        descricaoStatus = reserva.getStatusReserva().getDescricaoStatus();
        valorGasto = reserva.getValorGasto();
        milhasGastadas = reserva.getMilhasGastadas();
        numeroAssento= reserva.getNumeroAssento();
        idUsuario = reserva.getIdUsuario();
        idTransacao = reserva.getIdTransacao();
        messageType = "ReservaCommand";
    }
}

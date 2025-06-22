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
    private String codigoReserva;
    private String codigoVoo;
    private ZonedDateTime data;

    //status da reserva
    private UUID idEstadoCommand;
    private Integer codigoEstado;
    private String acronimoEstado;
    private String descricaoEstado;

    // *****
    private BigDecimal valor;
    private Integer milhasUtilizadas;
    private Integer quantidadePoltronas;
    private String codigoCliente;
    private UUID idTransacao;
    private String messageType;

    public ReservaCommand(Reserva reserva) {
        idReservaCommand = reserva.getIdReserva();
        codigoReserva = reserva.getCodigo();
        codigoVoo = reserva.getCodigoVoo();
        data = reserva.getData();
        idEstadoCommand= reserva.getEstado().getIdEstado();
        codigoEstado = reserva.getEstado().getCodigoEstado();
        acronimoEstado = reserva.getEstado().getAcronimoEstado();
        descricaoEstado = reserva.getEstado().getDescricaoEstado();
        valor = reserva.getValor();
        milhasUtilizadas = reserva.getMilhasUtilizadas();
        quantidadePoltronas= reserva.getQuantidadePoltronas();
        codigoCliente = reserva.getCodigoCliente();
        messageType = "ReservaCommand";
    }
}

package com.topus.reservas_query_service.dto.response;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.topus.reservas_query_service.model.ReservaQuery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R09ResDTO {
    private UUID idReserva;
    private String dataReserva;
    private String horaReserva;
    private String codReserva;
    private BigDecimal valorGasto;
    private Integer milhasGastadas;
    private String statusDescricao;
    private String codVoo;

    public R09ResDTO(ReservaQuery entity) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        ZonedDateTime localTimeReserva = entity.getDataReserva().withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));

        idReserva = entity.getIdReserva();
        dataReserva = localTimeReserva.format(dateFormatter);
        horaReserva = localTimeReserva.format(timeFormatter);
        codReserva = entity.getCodReserva();
        valorGasto = entity.getValorGasto();
        milhasGastadas = entity.getMilhasGastadas();
        statusDescricao = entity.getStatusDescricao();
        codVoo = entity.getCodVoo();
    }
}

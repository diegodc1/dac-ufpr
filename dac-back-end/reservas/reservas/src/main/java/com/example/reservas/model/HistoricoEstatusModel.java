package com.example.reservas.model;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricoEstatusModel implements Serializable {

    private UUID id;
    private ZonedDateTime dataAltEstado;
    private ReservaModel reserva;
    private EstadoReservaModel estadoInicial;
    private EstadoReservaModel estadoFinal;
}

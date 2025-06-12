package com.topus.reservas_query_service.sagas.command;


import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


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
    //**** *  status da reserva
    private UUID idStatusCommand; 
    private Integer codStatus;
    private String statusAcronym;
    private String statusDescricao;

    // ***********
    private BigDecimal valorGasto;
    private Integer milhasGastadas;
    private Integer numeroAssento;
    private String idUsuario;
    private UUID idTransacao;
    private String messageType;

    
}

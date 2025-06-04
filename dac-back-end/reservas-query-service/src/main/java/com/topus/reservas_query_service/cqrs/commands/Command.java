package com.topus.reservas_query_service.cqrs.commands;

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
public class Command {
    private UUID idReserva; 
    private UUID idTroca;
    private ZonedDateTime dataTroca;
    private UUID idIStatusCommand;
    private Integer codIStatus;
    private String iStatusAcronym;
    private String descricaoStatusI;
    private UUID idFStatusCommand;
    private Integer codFStatus;
    private String fStatusAcronym;
    private String fStatusDescricao;
    private String messageType;
}

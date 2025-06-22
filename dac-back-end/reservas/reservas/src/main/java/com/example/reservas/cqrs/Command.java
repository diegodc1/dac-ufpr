package com.example.reservas.cqrs;



import java.time.ZonedDateTime;
import java.util.UUID;

import com.example.reservas.model.HistoricoEstatus;

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
    private UUID idAltEstado;
    private ZonedDateTime dataAltEstado;
    private UUID iIdCommandEstado;
    private Integer iCodigoEstado;
    private String iAcronimoEstado;
    private String iDescricaoEstado;
    private UUID fIdCommandEstado;
    private Integer fCodigoEstado;
    private String fAcronimoEstado;
    private String fDescricaoEstado;
    private String messageType;

    public Command(HistoricoEstatus  novoHistorico) {
        idReserva = novoHistorico.getReserva().getIdReserva();
        idAltEstado = novoHistorico.getId();
        dataAltEstado = novoHistorico.getDataAltEstado();
        iIdCommandEstado = novoHistorico.getEstadoInicial().getIdEstado();
        iCodigoEstado = novoHistorico.getEstadoInicial().getCodigoEstado();
        iAcronimoEstado= novoHistorico.getEstadoInicial().getAcronimoEstado();
        iDescricaoEstado = novoHistorico.getEstadoInicial().getDescricaoEstado();
        fIdCommandEstado = novoHistorico.getEstadoFinal().getIdEstado();
        fCodigoEstado = novoHistorico.getEstadoFinal().getCodigoEstado();
        fAcronimoEstado= novoHistorico.getEstadoFinal().getAcronimoEstado();
        fDescricaoEstado = novoHistorico.getEstadoFinal().getDescricaoEstado();
        messageType = "Command sincro...";
    }
}

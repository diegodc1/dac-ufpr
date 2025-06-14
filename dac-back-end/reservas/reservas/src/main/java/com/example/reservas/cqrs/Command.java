package com.example.reservas.cqrs;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.example.reservas.entity.HistoricoEstatusEntity;
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

    public Command(HistoricoEstatusEntity novoHistorico) {
        this.idReserva = novoHistorico.getReserva().getIdReserva();
        this.idAltEstado = novoHistorico.getIdHistorico();
        this.dataAltEstado = novoHistorico.getDataAltEstado();

        this.iIdCommandEstado = novoHistorico.getEstadoInicial().getIdEstado();
        this.iCodigoEstado = novoHistorico.getEstadoInicial().getCodigoEstado();
        this.iAcronimoEstado = novoHistorico.getEstadoInicial().getAcronimoEstado();
        this.iDescricaoEstado = novoHistorico.getEstadoInicial().getDescricaoEstado();

        this.fIdCommandEstado = novoHistorico.getEstadoFinal().getIdEstado();
        this.fCodigoEstado = novoHistorico.getEstadoFinal().getCodigoEstado();
        this.fAcronimoEstado = novoHistorico.getEstadoFinal().getAcronimoEstado();
        this.fDescricaoEstado = novoHistorico.getEstadoFinal().getDescricaoEstado();

        this.messageType = "Command sincro...";
    }
}

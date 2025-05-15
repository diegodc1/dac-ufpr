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
    private UUID idAltStatus;
    private ZonedDateTime dataAltStatus;
    private UUID iIdCommandStatus;
    private Integer iStatusCode;
    private String iAcronimoStatus;
    private String iDescricaoStatus;
    private UUID fIdCommandStatus;
    private Integer fStatusCode;
    private String fAcronimoStatus;
    private String fDescricaoStatus;
    private String messageType;

    public Command(HistoricoEstatus  novoHistorico) {
        idReserva = novoHistorico.getReserva().getIdReserva();
        idAltStatus = novoHistorico.getId();
        dataAltStatus = novoHistorico.getDataAltStatus();
        iIdCommandStatus = novoHistorico.getStatusInicial().getIdStatus();
        iStatusCode = novoHistorico.getStatusInicial().getCodStatus();
        iAcronimoStatus= novoHistorico.getStatusInicial().getAcronimoStatus();
        iDescricaoStatus = novoHistorico.getStatusInicial().getDescricaoStatus();
        fIdCommandStatus = novoHistorico.getStatusFinal().getIdStatus();
        fStatusCode = novoHistorico.getStatusFinal().getCodStatus();
        fAcronimoStatus= novoHistorico.getStatusFinal().getAcronimoStatus();
        fDescricaoStatus = novoHistorico.getStatusFinal().getDescricaoStatus();
        messageType = "SynCommand";
    }
}

package com.example.reservas.cqrs;

import java.time.LocalDateTime;

public class CriarReservaCommand {
    private String codigoVoo;
    private LocalDateTime dataHoraReserva;

    public CriarReservaCommand(String codigoVoo, LocalDateTime dataHoraReserva) {
        this.codigoVoo = codigoVoo;
        this.dataHoraReserva = dataHoraReserva;
    }

    public String getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(String codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public LocalDateTime getDataHoraReserva() {
        return dataHoraReserva;
    }

    public void setDataHoraReserva(LocalDateTime dataHoraReserva) {
        this.dataHoraReserva = dataHoraReserva;
    }
}

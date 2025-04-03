package com.example.reservas.dto;

import java.time.LocalDateTime;

public class ReservaDTO {

    private Long id;
    private String codigoVoo;
    private LocalDateTime dataHoraReserva;
    private Long estadoReservaId;

  
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getEstadoReservaId() {
        return estadoReservaId;
    }

    public void setEstadoReservaId(Long estadoReservaId) {
        this.estadoReservaId = estadoReservaId;
    }
}

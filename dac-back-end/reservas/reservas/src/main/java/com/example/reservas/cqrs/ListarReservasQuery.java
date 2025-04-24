package com.example.reservas.cqrs;

import java.time.LocalDate;

public class ListarReservasQuery {
    private String codigoVoo;
    private LocalDate dataReserva;

    public ListarReservasQuery() {
    }

    public ListarReservasQuery(String codigoVoo, LocalDate dataReserva) {
        this.codigoVoo = codigoVoo;
        this.dataReserva = dataReserva;
    }

    public String getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(String codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }
}

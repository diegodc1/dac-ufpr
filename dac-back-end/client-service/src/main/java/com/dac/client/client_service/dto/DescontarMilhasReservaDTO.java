package com.dac.client.client_service.dto;

import lombok.Data;

@Data
public class DescontarMilhasReservaDTO {
    private int quantidade;
    private double valorPago;

    private String aeroporto_origem;

    private String aeroporto_destino;
    private String codigo_reserva;

}

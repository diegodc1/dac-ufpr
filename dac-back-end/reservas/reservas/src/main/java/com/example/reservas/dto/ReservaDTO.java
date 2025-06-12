package com.example.reservas.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaDTO {
    private String codigo;
    private LocalDateTime data;
    private BigDecimal valor;
    private int milhasUtilizadas;
    private int quantidadePoltronas;
    private int codigoCliente;
    private String estado;
    private String codigoVoo;
    private String codigoAeroportoOrigem;
    private String codigoAeroportoDestino;
}

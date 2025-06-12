package com.example.reservas.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaModel {
    private UUID idReserva;
    private String codigo;
    private String codigoVoo;
    private ZonedDateTime data;

    
    private String estado;

    private BigDecimal valor;
    private Integer milhasUtilizadas;
    private Integer quantidadePoltronas;
    private String codigoCliente;
    private UUID idTransacao;


    private String aeroportoOrigemCodigo;
    private String aeroportoDestinoCodigo;
}

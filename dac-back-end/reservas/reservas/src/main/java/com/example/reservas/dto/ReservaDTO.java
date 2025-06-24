package com.example.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.reservas.model.Reserva;


import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {

    private UUID idReserva;
    private String codigo;
    private String codigoVoo;
    private ZonedDateTime data;
    private String estado;  // Você pode retornar apenas o nome ou descrição do estado
    private BigDecimal valor;
    private Integer milhasUtilizadas;
    private Integer quantidadePoltronas;
    private Integer codigoCliente;
    private UUID idTransacao;
    private String aeroportoOrigem;  // A mesma coisa para o aeroporto
    private String aeroportoDestino;

    // Método que converte uma Reserva em ReservaDTO
    public static ReservaDTO fromEntity(Reserva reserva) {
        return new ReservaDTO(
            reserva.getIdReserva(),
            reserva.getCodigo(),
            reserva.getCodigoVoo(),
            reserva.getData(),
            reserva.getEstado() != null ? reserva.getEstado().getDescricaoEstado() : null,  // Acessando a descrição do estado
            reserva.getValor(),
            reserva.getMilhasUtilizadas(),
            reserva.getQuantidadePoltronas(),
            reserva.getCodigoCliente(),
            reserva.getIdTransacao(),
            reserva.getAeroportoOrigem() != null ? reserva.getAeroportoOrigem().getCodigo() : null, // Acessando código do aeroporto
            reserva.getAeroportoDestino() != null ? reserva.getAeroportoDestino().getCodigo() : null // Acessando código do aeroporto
        );
    }
}

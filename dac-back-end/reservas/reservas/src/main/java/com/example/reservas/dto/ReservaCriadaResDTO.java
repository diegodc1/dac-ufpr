package com.example.reservas.dto;

import com.example.reservas.model.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Getter
@Setter
public class ReservaCriadaResDTO {
    private String codigo;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime data;
    private BigDecimal valor;
    private Integer milhas_utilizadas;
    private Integer quantidade_poltronas;
    private Long codigo_cliente;
    private String estado;
    private String codigo_voo;

    public ReservaCriadaResDTO(Reserva reserva) {
        this.codigo = reserva.getCodigo();
        this.data = reserva.getData();
        this.valor = reserva.getValor();
        this.milhas_utilizadas = reserva.getMilhasUtilizadas();
        this.quantidade_poltronas = reserva.getQuantidadePoltronas();
        this.codigo_cliente = Long.valueOf(reserva.getCodigoCliente());
        this.estado = reserva.getEstado().getDescricaoEstado();
        this.codigo_voo = reserva.getCodigoVoo();
    }
}

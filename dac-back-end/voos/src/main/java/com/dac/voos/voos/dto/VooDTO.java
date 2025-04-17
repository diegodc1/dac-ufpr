package com.dac.voos.voos.dto;

import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.EstadoVoo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VooDTO(
        LocalDateTime data_hora,
        Aeroporto aeroportoOrigem,
        Aeroporto aeroportoDestino,
        BigDecimal valorPassagem,
        int quantidadePoltronasTotal,
        int quantidadePoltronasOculpadas,
        EstadoVoo estadoVoo) {

}

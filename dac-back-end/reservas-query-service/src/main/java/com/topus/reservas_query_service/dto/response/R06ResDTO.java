package com.topus.reservas_query_service.dto.response;

import java.math.BigDecimal;

import com.topus.reservas_query_service.model.ReservaQuery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R06ResDTO {
    private String codVoo;
    private BigDecimal valorGasto;
    private Integer milhasGastadas;

    public R06ResDTO(ReservaQuery reservaQuery) {
        codVoo = reservaQuery.getCodVoo();
        valorGasto = reservaQuery.getValorGasto();
        milhasGastadas= reservaQuery.getMilhasGastadas();
    }
}

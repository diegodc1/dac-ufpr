package com.topus.reservas_query_service.dto.response;


import java.util.UUID;

import com.topus.reservas_query_service.model.ReservaQuery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R03ResDTO {
    private UUID idReserva;
    private String codReserva; // request key for flight service
    private String codVoo;
    private String statusDescricao;

    public R03ResDTO(ReservaQuery entity) {
        idReserva = entity.getIdReserva();
        codReserva= entity.getCodReserva();
        codVoo= entity.getCodVoo();
        statusDescricao = entity.getStatusDescricao();
    }
}



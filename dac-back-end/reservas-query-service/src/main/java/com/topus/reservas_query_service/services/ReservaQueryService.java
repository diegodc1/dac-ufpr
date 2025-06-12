package com.topus.reservas_query_service.services;


import java.util.List;
import java.util.UUID;

import com.topus.reservas_query_service.cqrs.commands.Command;
import com.topus.reservas_query_service.dto.response.R03ResDTO;
import com.topus.reservas_query_service.dto.response.R04ResDTO;
import com.topus.reservas_query_service.dto.response.R09ResDTO;

public interface ReservaQueryService {

    // R03 - 2 from controller
    List<R03ResDTO> findReservasCliente(String idUsuario);

    // R04 from controller
    R04ResDTO getReserva(String idReserva);

    // R06 - 2
    List<String> getCodsVoo(List<UUID> IdsTransaction);

    // R09 - 1
    R09ResDTO searchReserva(String codReserva);

    // R10, R12 from cqrs
    void syncronizeDBs(Command command);
}

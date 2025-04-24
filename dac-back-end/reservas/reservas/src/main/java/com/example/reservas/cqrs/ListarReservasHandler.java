package com.example.reservas.cqrs;

import com.example.reservas.dto.ReservaDTO;
import com.example.reservas.entitys.ReservaEntity;
import com.example.reservas.repositorys.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListarReservasHandler {

    @Autowired
    private ReservaRepository reservaRepository;

    public List<ReservaDTO> handle(ListarReservasQuery query) {
        List<ReservaEntity> reservas = reservaRepository.findAll();

        return reservas.stream()
                .filter(r -> query.getCodigoVoo() == null || r.getCodigoVoo().equalsIgnoreCase(query.getCodigoVoo()))
                .filter(r -> query.getDataReserva() == null || 
                             r.getDataHoraReserva().toLocalDate().equals(query.getDataReserva()))
                .map(entity -> {
                    ReservaDTO dto = new ReservaDTO();
                    dto.setId(entity.getId());
                    dto.setCodigoVoo(entity.getCodigoVoo());
                    dto.setDataHoraReserva(entity.getDataHoraReserva());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

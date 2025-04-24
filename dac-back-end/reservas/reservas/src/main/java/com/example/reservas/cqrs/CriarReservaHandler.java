package com.example.reservas.cqrs;

import com.example.reservas.dto.ReservaDTO;
import com.example.reservas.entitys.ReservaEntity;
import com.example.reservas.repositorys.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarReservaHandler {

    @Autowired
    private ReservaRepository reservaRepository;

    public ReservaDTO handle(CriarReservaCommand command) {
        ReservaEntity entity = new ReservaEntity();
        entity.setCodigoVoo(command.getCodigoVoo());
        entity.setDataHoraReserva(command.getDataHoraReserva());

        ReservaEntity saved = reservaRepository.save(entity);

        ReservaDTO dto = new ReservaDTO();
        dto.setId(saved.getId());
        dto.setCodigoVoo(saved.getCodigoVoo());
        dto.setDataHoraReserva(saved.getDataHoraReserva());
        return dto;
    }
}

package com.example.reservas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reservas.dto.ReservaDTO;
import com.example.reservas.entitys.ReservaEntity;
import com.example.reservas.repositorys.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    
    public ReservaDTO criarReserva(ReservaDTO reservaDTO) {
      
        ReservaEntity reserva = new ReservaEntity();
        reserva.setCodigoVoo(reservaDTO.getCodigoVoo());
        reserva.setDataHoraReserva(reservaDTO.getDataHoraReserva());
        
      
        reserva = reservaRepository.save(reserva);
        
       
        reservaDTO.setId(reserva.getId());
        return reservaDTO;
    }

    
    public List<ReservaDTO> listarReservas() {
       
        List<ReservaEntity> reservas = reservaRepository.findAll();
        
        return reservas.stream()
                .map(reserva -> {
                    ReservaDTO dto = new ReservaDTO();
                    dto.setId(reserva.getId());
                    dto.setCodigoVoo(reserva.getCodigoVoo());
                    dto.setDataHoraReserva(reserva.getDataHoraReserva());
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

    
    public ReservaDTO obterReservaPorId(Long id) {
       
        ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        
        
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCodigoVoo(reserva.getCodigoVoo());
        dto.setDataHoraReserva(reserva.getDataHoraReserva());
       
        return dto;
    }
}

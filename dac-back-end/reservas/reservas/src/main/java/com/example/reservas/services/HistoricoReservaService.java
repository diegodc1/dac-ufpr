package com.example.reservas.services;

import com.example.reservas.dto.HistoricoReservaDTO;
import com.example.reservas.entitys.HistoricoReservaEntity;
import com.example.reservas.repositorys.HistoricoReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoReservaService {

    @Autowired
    private HistoricoReservaRepository historicoReservaRepository;

    
    public HistoricoReservaDTO criarHistorico(HistoricoReservaDTO historicoReservaDTO) {
        HistoricoReservaEntity historico = new HistoricoReservaEntity();
        historico.setReservaId(historicoReservaDTO.getReservaId());
        historico.setEstadoOrigem(historicoReservaDTO.getEstadoOrigem());
        historico.setEstadoDestino(historicoReservaDTO.getEstadoDestino());
        historico.setDataHoraAlteracao(historicoReservaDTO.getDataHoraAlteracao());
        historico = historicoReservaRepository.save(historico);
        historicoReservaDTO.setId(historico.getId());
        return historicoReservaDTO;
    }

    public List<HistoricoReservaDTO> listarHistoricoPorReserva(Long reservaId) {
        List<HistoricoReservaEntity> historicos = historicoReservaRepository.findByReservaId(reservaId);
        return historicos.stream()
                .map(historico -> {
                    HistoricoReservaDTO dto = new HistoricoReservaDTO();
                    dto.setId(historico.getId());
                    dto.setReservaId(historico.getReservaId());
                    dto.setEstadoOrigem(historico.getEstadoOrigem());
                    dto.setEstadoDestino(historico.getEstadoDestino());
                    dto.setDataHoraAlteracao(historico.getDataHoraAlteracao());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}




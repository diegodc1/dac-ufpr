package com.example.reservas.services;

import com.example.reservas.dto.ReservaDTO;
import com.example.reservas.entity.AeroportoEntity;
import com.example.reservas.entity.EstadoReservaEntity;
import com.example.reservas.entity.ReservaEntity;
import com.example.reservas.repositorys.AeroportoRepository;
import com.example.reservas.repositorys.EstadoReservaRepository;
import com.example.reservas.repositorys.ReservaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final EstadoReservaRepository estadoReservaRepository;
    private final AeroportoRepository aeroportoRepository;
    private final ModelMapper modelMapper;

    public ReservaService(ReservaRepository reservaRepository,
                          EstadoReservaRepository estadoReservaRepository,
                          AeroportoRepository aeroportoRepository,
                          ModelMapper modelMapper) {
        this.reservaRepository = reservaRepository;
        this.estadoReservaRepository = estadoReservaRepository;
        this.aeroportoRepository = aeroportoRepository;
        this.modelMapper = modelMapper;
    }

    public ReservaDTO criar(ReservaDTO dto) {
        // Buscar dados relacionados
        EstadoReservaEntity estado = estadoReservaRepository.findByDescricaoEstado("CONFIRMADA")
                .orElseThrow(() -> new RuntimeException("Estado 'CONFIRMADA' não encontrado"));

        AeroportoEntity origem = aeroportoRepository.findById(dto.getCodigoAeroportoOrigem())
                .orElseThrow(() -> new RuntimeException("Aeroporto origem não encontrado"));

        AeroportoEntity destino = aeroportoRepository.findById(dto.getCodigoAeroportoDestino())
                .orElseThrow(() -> new RuntimeException("Aeroporto destino não encontrado"));

        // Mapear DTO para Entity
        ReservaEntity entity = modelMapper.map(dto, ReservaEntity.class);
        entity.setIdReserva(UUID.randomUUID());
        entity.setCodigo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        entity.setIdTransacao(UUID.randomUUID());
        entity.setEstado(estado);
        entity.setAeroportoOrigem(origem);
        entity.setAeroportoDestino(destino);
        entity.setData(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));

        // Salvar e retornar DTO
        ReservaEntity salva = reservaRepository.save(entity);
        return modelMapper.map(salva, ReservaDTO.class);
    }

    public ReservaDTO buscarPorCodigo(String codigo) {
        ReservaEntity reserva = reservaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        return modelMapper.map(reserva, ReservaDTO.class);
    }

    public ReservaDTO atualizarEstado(String codigo, String estadoNovo) {
        ReservaEntity reserva = reservaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        EstadoReservaEntity estado = estadoReservaRepository.findByDescricaoEstado(estadoNovo)
                .orElseThrow(() -> new RuntimeException("Estado '" + estadoNovo + "' não encontrado"));

        reserva.setEstado(estado);
        return modelMapper.map(reservaRepository.save(reserva), ReservaDTO.class);
    }

    public ReservaDTO cancelar(String codigo) {
        return atualizarEstado(codigo, "CANCELADA");
    }
}

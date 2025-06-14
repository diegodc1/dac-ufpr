package com.example.reservas.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.reservas.cqrs.Command;

import com.example.reservas.dto.CheckinDTO;
import com.example.reservas.entity.AeroportoEntity;
import com.example.reservas.entity.EstadoReservaEntity;
import com.example.reservas.entity.HistoricoEstatusEntity;
import com.example.reservas.entity.ReservaEntity;
import com.example.reservas.exceptions.ReservaNaoEncontradoException;
import com.example.reservas.repositorys.AeroportoRepository;
import com.example.reservas.repositorys.EstadoReservaRepository;
import com.example.reservas.repositorys.HistoricoStatusRepository;
import com.example.reservas.repositorys.ReservaRepository;
import com.example.reservas.sagas.commands.CriarReserva;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommandServiceImplement implements CommandService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EstadoReservaRepository estadoReservaRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Override
    @Transactional
    public String criarReserva(CriarReserva command) {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setIdReserva(UUID.randomUUID());
        reserva.setCodigo(gerarCodigoReserva());
        reserva.setCodigoCliente(command.getCodigoCliente());
        reserva.setValor(command.getValor());
        reserva.setMilhasUtilizadas(command.getMilhasUtilizadas());
        reserva.setQuantidadePoltronas(command.getQuantidadePoltronas());
        reserva.setCodigoVoo(command.getCodigoVoo());
        reserva.setData(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
        reserva.setIdTransacao(UUID.randomUUID());

        AeroportoEntity origem = aeroportoRepository.findById(command.getCodigoAeroportoOrigem())
                .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado"));

        AeroportoEntity destino = aeroportoRepository.findById(command.getCodigoAeroportoDestino())
                .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado"));

        EstadoReservaEntity estado = estadoReservaRepository.findByCodigoEstado(1)
                .orElseThrow(() -> new RuntimeException("Estado CONFIRMADA não encontrado"));

        reserva.setAeroportoOrigem(origem);
        reserva.setAeroportoDestino(destino);
        reserva.setEstado(estado);

        reservaRepository.save(reserva);
        return reserva.getCodigo();
    }

    @Override
    @Transactional
    public void cancelarReserva(String codigoReserva) {
        ReservaEntity reserva = reservaRepository.findByCodigo(codigoReserva)
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada: " + codigoReserva));

        EstadoReservaEntity estadoCancelado = estadoReservaRepository.findByCodigoEstado(3)
                .orElseThrow(() -> new RuntimeException("Estado CANCELADO não encontrado"));

        reserva.setEstado(estadoCancelado);
        reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public CheckinDTO atualizarEstado(String identifier, String estado) throws JsonProcessingException {
        int codigoEstado = mapearEstadoParaCodigo(estado);

        ReservaEntity reserva = identifier.length() == 36
                ? reservaRepository.findById(UUID.fromString(identifier))
                        .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para ID: " + identifier))
                : reservaRepository.findByCodigo(identifier)
                        .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para código: " + identifier));

        EstadoReservaEntity estadoInicial = reserva.getEstado();
        EstadoReservaEntity estadoFinal = estadoReservaRepository.findByCodigoEstado(codigoEstado)
                .orElseThrow(() -> new RuntimeException("Estado não encontrado: " + estado));

        reserva.setEstado(estadoFinal);
        reservaRepository.save(reserva);

        HistoricoEstatusEntity historico = HistoricoEstatusEntity.builder()
                .dataAltEstado(ZonedDateTime.now(ZoneId.of("UTC")))
                .reserva(reserva)
                .estadoInicial(estadoInicial)
                .estadoFinal(estadoFinal)
                .build();

        historicoStatusRepository.save(historico);

        Command commandMessage = new Command(historico);
        String message = objectMapper.writeValueAsString(commandMessage);
        rabbitTemplate.convertAndSend("ReservaQueryRequestChannel", message);

        return CheckinDTO.builder()
                .idReserva(reserva.getIdReserva())
                .codigoReserva(reserva.getCodigo())
                .estadoInicial(estadoInicial.getDescricaoEstado())
                .estadoAtual(estadoFinal.getDescricaoEstado())
                .build();
    }

    private int mapearEstadoParaCodigo(String estado) {
        return switch (estado.toUpperCase()) {
            case "CONFIRMADA" -> 1;
            case "CHECK-IN"   -> 2;
            case "CANCELADA", "CANCELADO" -> 3;
            case "EMBARCADA"  -> 4;
            case "REALIZADA", "REALIZADO" -> 5;
            default -> throw new IllegalArgumentException("Estado inválido: " + estado);
        };
    }

    private String gerarCodigoReserva() {
        Random random = new Random();
        StringBuilder letras = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            letras.append((char) ('A' + random.nextInt(26)));
        }
        int numeros = 100 + random.nextInt(900);
        return letras.toString() + numeros;
    }
}

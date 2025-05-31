package com.example.reservas.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.reservas.cqrs.Command;
import com.example.reservas.dto.CheckinDTO;
import com.example.reservas.exceptions.ReservaNaoEncontradoException;
import com.example.reservas.model.HistoricoEstatus;
import com.example.reservas.model.Reserva;
import com.example.reservas.model.StatusReserva;
import com.example.reservas.repositorys.HistoricoStatusRepository;
import com.example.reservas.repositorys.ReservaRepository;
import com.example.reservas.repositorys.StatusReservaRepository;
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
    private StatusReservaRepository statusReservaRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Override
    @Transactional
    public String criarReserva(CriarReserva command) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(UUID.randomUUID());
        reserva.setCodigoReserva(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        reserva.setCodigoCliente(command.getCodigoCliente());
        reserva.setValor(command.getValor());
        reserva.setMilhasUtilizadas(command.getMilhasUtilizadas());
        reserva.setQuantidadePoltronas(command.getQuantidadePoltronas());
        reserva.setCodigoVoo(command.getCodigoVoo());
        reserva.setCodigoAeroportoOrigem(command.getCodigoAeroportoOrigem());
        reserva.setCodigoAeroportoDestino(command.getCodigoAeroportoDestino());

        StatusReserva estadoCriada = statusReservaRepository.findByCodigoEstado(1); // CRIADA = 1
        reserva.setEstadoReserva(estadoCriada);

        reservaRepository.save(reserva);
        return reserva.getCodigoReserva();
    }

    @Override
    @Transactional
    public void cancelarReserva(String codigoReserva) {
        Reserva reserva = reservaRepository.getByCodigoReserva(codigoReserva)
            .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada: " + codigoReserva));

        StatusReserva estadoCancelada = statusReservaRepository.findByCodigoEstado(4); // CANCELADA = 4
        reserva.setEstadoReserva(estadoCancelada);
        reservaRepository.save(reserva);

        // Opcional: histórico e envio por RabbitMQ
    }

    @Override
    @Transactional
    public CheckinDTO atualizarEstadoReserva(String identifier, String estado) throws JsonProcessingException {
        int codigoEstado = mapearEstadoParaCodigo(estado);

        Reserva reserva = identifier.length() == 36
            ? reservaRepository.findById(UUID.fromString(identifier))
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para ID: " + identifier))
            : reservaRepository.getByCodigoReserva(identifier)
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para código: " + identifier));

        StatusReserva estadoInicial = reserva.getEstadoReserva();
        StatusReserva estadoFinal = statusReservaRepository.findByCodigoEstado(codigoEstado);

        reserva.setEstadoReserva(estadoFinal);
        reservaRepository.save(reserva);

        HistoricoEstatus historico = HistoricoEstatus.builder()
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
            .codigoReserva(reserva.getCodigoReserva())
            .estadoInicial(estadoInicial.getDescricaoEstado())
            .estadoAtual(estadoFinal.getDescricaoEstado())
            .build();
    }

    private int mapearEstadoParaCodigo(String estado) {
        switch (estado.toUpperCase()) {
            case "CRIADA":
                return 1;
            case "CHECK-IN":
                return 2;
            case "EMBARCADA":
                return 3;
            case "CANCELADA":
                return 4;
            default:
                throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }
}

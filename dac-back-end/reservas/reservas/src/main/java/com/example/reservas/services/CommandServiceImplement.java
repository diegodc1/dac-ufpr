package com.example.reservas.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.reservas.repositorys.StatusReservaRepository;
import com.example.reservas.cqrs.Command;
import com.example.reservas.dto.CheckinDTO;

import com.example.reservas.exceptions.ReservaNaoEncontradoException;
import com.example.reservas.model.Reserva;
import com.example.reservas.model.StatusReserva;
import com.example.reservas.model.HistoricoEstatus;
import com.example.reservas.repositorys.ReservaRepository;
import com.example.reservas.repositorys.HistoricoStatusRepository;
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
    private StatusReservaRepository StatusReservaRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Override
    @Transactional
    public CheckinDTO updateStatusReserva(String identifier, int codStatus) throws JsonProcessingException {

        // encontrar uma reserva
        Reserva reserva= identifier.length() == 35
                ? reservaRepository.findById(UUID.fromString(identifier)).orElseThrow(
                        () -> new ReservaNaoEncontradoException("Reserva não encontrado  para esse ID: " + identifier))
                : reservaRepository.getRervaByCod(identifier).orElseThrow(
                        () -> new ReservaNaoEncontradoException("Reserva não encontra para esse código: " + identifier));

        StatusReserva initialStatus = reserva.getStatusReserva();

        // atulizar uma reserva
        reserva.setStatusReserva(StatusReservaRepository.findByCodStatus(codStatus));
        reserva = reservaRepository.save(reserva);

        StatusReserva statusFinal = reserva.getStatusReserva();

        // Criar registro de histórico
        HistoricoEstatus novoHistorico = HistoricoEstatus.builder()
                .dataAltStatus(ZonedDateTime.now(ZoneId.of("UTC")))
                .reserva(reserva)
                .statusInicial(initialStatus)
                .statusFinal(statusFinal)
                .build();

        novoHistorico = historicoStatusRepository.save(novoHistorico);

        // Prepara a mensagem para o serviço de consulta e envia
        Command commandMessage = new Command(novoHistorico);

        String message = objectMapper.writeValueAsString(commandMessage);
        rabbitTemplate.convertAndSend("BookingQueryRequestChannel", message);

        // dto de resposta para o cliente
        CheckinDTO dto = CheckinDTO.builder()
                .idReserva(novoHistorico.getReserva().getIdReserva())
                .codReserva(novoHistorico.getReserva().getCodReserva())
                .statusInicial(novoHistorico.getStatusInicial().getDescricaoStatus())
                .statusAtual(novoHistorico.getStatusFinal().getDescricaoStatus())
                .build();

        return dto;
    }

}

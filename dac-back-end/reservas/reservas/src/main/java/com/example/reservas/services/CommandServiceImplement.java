/* package com.example.reservas.services;

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
import com.example.reservas.exceptions.ReservaNaoEncontradoException;
import com.example.reservas.model.Aeroporto;
import com.example.reservas.model.HistoricoEstatus;
import com.example.reservas.model.Reserva;
import com.example.reservas.model.EstadoReserva;
import com.example.reservas.repositorys.AeroportoRepository;
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
    private AeroportoRepository aeroportoRepository;


    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Override
    @Transactional
    public String criarReserva(CriarReserva command) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(UUID.randomUUID());
        reserva.setCodigo(gerarCodigoReserva());  // Código de 3 letras + 3 números
        reserva.setCodigoCliente(command.getCodigoCliente());
        reserva.setValor(command.getValor());
        reserva.setMilhasUtilizadas(command.getMilhasUtilizadas());
        reserva.setQuantidadePoltronas(command.getQuantidadePoltronas());
        reserva.setCodigoVoo(command.getCodigoVoo());
        Aeroporto origem = aeroportoRepository.findById(command.getCodigoAeroportoOrigem())
        .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado"));

            Aeroporto destino = aeroportoRepository.findById(command.getCodigoAeroportoDestino())
    .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado"));

        reserva.setAeroportoOrigem(origem);
        reserva.setAeroportoDestino(destino);


        EstadoReserva estadoCriada = statusReservaRepository.findByCodigoEstado(1); // CONFIRMADA = 1
        reserva.setEstado(estadoCriada);

        reservaRepository.save(reserva);
        return reserva.getCodigo();
    }

    @Override
    @Transactional
    public void cancelarReserva(String codigoReserva) {
        Reserva reserva = reservaRepository.getByCodigo(codigoReserva)
            .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada: " + codigoReserva));

        EstadoReserva estadoCancelada = statusReservaRepository.findByCodigoEstado(3); // CANCELADO = 3
        reserva.setEstado(estadoCancelada);
        reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public CheckinDTO atualizarEstado(String identifier, String estado) throws JsonProcessingException {
        int codigoEstado = mapearEstadoParaCodigo(estado);

        Reserva reserva = identifier.length() == 36
            ? reservaRepository.findById(UUID.fromString(identifier))
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para ID: " + identifier))
            : reservaRepository.getByCodigo(identifier)
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para código: " + identifier));

        EstadoReserva estadoInicial = reserva.getEstado();
        EstadoReserva estadoFinal = statusReservaRepository.findByCodigoEstado(codigoEstado);

        reserva.setEstado(estadoFinal);
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
            .codigoReserva(reserva.getCodigo())
            .estadoInicial(estadoInicial.getDescricaoEstado())
            .estadoAtual(estadoFinal.getDescricaoEstado())
            .build();
    }

    private int mapearEstadoParaCodigo(String estado) {
        switch (estado.toUpperCase()) {
            case "CONFIRMADA": return 1;
            case "CHECK-IN":   return 2;
            case "EMBARCADA":  return 3;
            case "CANCELADO":  return 4;
            case "REALIZADO":  return 5;
            default:
                throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }

      Gera código de reserva com 3 letras aleatórias + 3 números (ex: ABC123)
    private String gerarCodigoReserva() {
        Random random = new Random();

        StringBuilder letras = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            letras.append((char) ('A' + random.nextInt(26)));
        }

        int numeros = 100 + random.nextInt(900); // Gera entre 100 e 999

        return letras.toString() + numeros;
    }
}
*/
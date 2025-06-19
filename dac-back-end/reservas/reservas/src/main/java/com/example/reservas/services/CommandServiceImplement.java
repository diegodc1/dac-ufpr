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
import com.example.reservas.repositorys.EstadoReservaRepository;
import com.example.reservas.repositorys.HistoricoStatusRepository;
import com.example.reservas.repositorys.ReservaRepository;
import com.example.reservas.sagas.commands.CriarReserva;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
    private HistoricoStatusRepository historicoStatusRepository;

    @Autowired
    private AeroportoService aeroportoService;

    // Método para criar reserva
    @Override
    @Transactional
    public String criarReserva(CriarReserva command) {
        // Criando uma nova reserva
        ReservaEntity reserva = new ReservaEntity();
        reserva.setCodigo(gerarCodigoReserva());
        reserva.setCodigoCliente(command.getCodigoCliente());
        reserva.setValor(command.getValor());
        reserva.setMilhasUtilizadas(command.getMilhasUtilizadas());
        reserva.setQuantidadePoltronas(command.getQuantidadePoltronas());
        reserva.setCodigoVoo(command.getCodigoVoo());
        reserva.setData(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
        reserva.setIdTransacao(UUID.randomUUID()); // O UUID pode ser gerado automaticamente, mas aqui definimos explicitamente.

        // Buscando aeroportos
        AeroportoEntity origem = aeroportoService.buscarPorCodigo(command.getCodigoAeroportoOrigem());
        AeroportoEntity destino = aeroportoService.buscarPorCodigo(command.getCodigoAeroportoDestino());

        // Buscando o estado "CONFIRMADA"
        EstadoReservaEntity estadoConfirmada = estadoReservaRepository.findByCodigoEstado(1)
                .orElseThrow(() -> new RuntimeException("Estado CONFIRMADA não encontrado"));

        // Associando entidades
        reserva.setAeroportoOrigem(origem);
        reserva.setAeroportoDestino(destino);
        reserva.setDescricao(estadoConfirmada);  // Aqui associamos o estado "CONFIRMADA".

        // Salvando a reserva
        reservaRepository.save(reserva);
        log.info("Reserva criada com sucesso, código: {}", reserva.getCodigo());

        return reserva.getCodigo();
    }

    // Método para atualizar o estado da reserva
    @Transactional
    public void atualizarEstadoReserva(String codigoReserva, String novoEstado) {
        // Buscando a reserva
        ReservaEntity reserva = reservaRepository.findByCodigo(codigoReserva)
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada"));

        // Buscando o estado atual e o novo estado
        EstadoReservaEntity estadoAtual = reserva.getDescricao();
        EstadoReservaEntity estadoNovo = estadoReservaRepository.findByDescricao(novoEstado.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Estado " + novoEstado + " não encontrado"));

        // Atualizando o estado da reserva
        reserva.setDescricao(estadoNovo);
        reservaRepository.save(reserva);
        log.info("Estado da reserva {} atualizado de {} para {}", reserva.getCodigo(), estadoAtual.getDescricao(), estadoNovo.getDescricao());

        // Registrando o histórico de mudança de estado
        HistoricoEstatusEntity historico = HistoricoEstatusEntity.builder()
                .dataAltEstado(ZonedDateTime.now(ZoneId.of("UTC")))
                .reserva(reserva)
                .estadoInicial(estadoAtual)
                .estadoFinal(estadoNovo)
                .build();
        historicoStatusRepository.save(historico);
        log.info("Histórico de status registrado para a reserva {}", reserva.getCodigo());
    }

    // Método para cancelar a reserva
    @Override
    @Transactional
    public void cancelarReserva(String codigoReserva) {
        // Buscando a reserva
        ReservaEntity reserva = reservaRepository.findByCodigo(codigoReserva)
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada: " + codigoReserva));

        // Buscando o estado "CANCELADO"
        EstadoReservaEntity estadoCancelado = estadoReservaRepository.findByCodigoEstado(3)
                .orElseThrow(() -> new RuntimeException("Estado CANCELADO não encontrado"));

        // Atualizando o estado da reserva
        reserva.setDescricao(estadoCancelado);
        reservaRepository.save(reserva);
        log.info("Reserva {} cancelada com sucesso.", reserva.getCodigo());
    }

    // Método para atualizar o estado de uma reserva (com check-in)
    @Override
    @Transactional
    public CheckinDTO atualizarEstado(String identifier, String descricao) throws JsonProcessingException {
        // Mapeando o estado para código
        int codigoEstado = mapearEstadoParaCodigo(descricao);

        // Buscando a reserva pelo ID ou código
        ReservaEntity reserva = identifier.length() == 36
                ? reservaRepository.findById(UUID.fromString(identifier))
                    .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para ID: " + identifier))
                : reservaRepository.findByCodigo(identifier)
                    .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para código: " + identifier));

        // Buscando o estado atual e o novo estado
        EstadoReservaEntity estadoInicial = reserva.getDescricao();
        EstadoReservaEntity estadoFinal = estadoReservaRepository.findByCodigoEstado(codigoEstado)
                .orElseThrow(() -> new RuntimeException("Estado não encontrado: " + descricao));

        // Atualizando o estado da reserva
        reserva.setDescricao(estadoFinal);
        reservaRepository.save(reserva);

        // Registrando o histórico de mudança de estado
        HistoricoEstatusEntity historico = HistoricoEstatusEntity.builder()
                .dataAltEstado(ZonedDateTime.now(ZoneId.of("UTC")))
                .reserva(reserva)
                .estadoInicial(estadoInicial)
                .estadoFinal(estadoFinal)
                .build();
        historicoStatusRepository.save(historico);

        // Enviando a mensagem para a fila de eventos
        Command commandMessage = new Command(historico);
        String message = objectMapper.writeValueAsString(commandMessage);
        rabbitTemplate.convertAndSend("ReservaQueryRequestChannel", message);

        log.info("Estado da reserva {} atualizado para {}.", reserva.getCodigo(), estadoFinal.getDescricao());

        return CheckinDTO.builder()
                .idReserva(reserva.getIdReserva())
                .codigoReserva(reserva.getCodigo())
                .estadoInicial(estadoInicial.getDescricao())
                .estadoAtual(estadoFinal.getDescricao())
                .build();
    }

    // Método para mapear o nome do estado para um código numérico
    private int mapearEstadoParaCodigo(String descricao) {
        return switch (descricao.toUpperCase()) {
            case "CONFIRMADA" -> 1;
            case "CHECK-IN" -> 2;
            case "CANCELADA", "CANCELADO" -> 3;
            case "EMBARCADA" -> 4;
            case "REALIZADA", "REALIZADO" -> 5;
            default -> throw new IllegalArgumentException("Estado inválido: " + descricao);
        };
    }

    // Método para gerar um código único para a reserva
    private String gerarCodigoReserva() {
        Random random = new Random();
        StringBuilder letras = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            letras.append((char) ('A' + random.nextInt(26)));  // Gerando letras aleatórias
        }
        int numeros = 100 + random.nextInt(900);  // Gerando números aleatórios
        return letras.toString() + numeros;
    }
}

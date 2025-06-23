package com.example.reservas.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.example.reservas.dto.ReservaCriadaResDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.reservas.exceptions.ReservaNaoEncontradoException;
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
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CommandServiceImplement.class);

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Override
    @Transactional
    public ReservaCriadaResDTO criarReserva(CriarReserva command) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(UUID.randomUUID());
        reserva.setCodigo(gerarCodigoReserva());  // Código de 3 letras + 3 números
        reserva.setCodigoCliente(command.getCodigo_cliente());
        reserva.setValor(command.getValor());
        reserva.setMilhasUtilizadas(command.getMilhas_utilizadas());
        reserva.setQuantidadePoltronas(command.getQuantidade_poltronas());
        reserva.setCodigoVoo(command.getCodigo_voo());
        reserva.setData(ZonedDateTime.now());
//        Aeroporto origem = aeroportoRepository.findById(command.getCodigo_aeroporto_origem())
//        .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado"));
//
//            Aeroporto destino = aeroportoRepository.findById(command.getCodigo_aeroporto_destino())
//    .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado"));
//
//        reserva.setAeroportoOrigem(origem);
//        reserva.setAeroportoDestino(destino);


        EstadoReserva estadoCriada = statusReservaRepository.findByCodigoEstado(6); // CRIADA = 6
        reserva.setEstado(estadoCriada);

        reservaRepository.save(reserva);


        ReservaCriadaResDTO reservaDto = new ReservaCriadaResDTO(reserva);
        return reservaDto;
    }

    @Override
    @Transactional
    public ReservaCriadaResDTO cancelarReserva(String codigoReserva) {
        Reserva reserva = reservaRepository.getByCodigo(codigoReserva)
            .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada: " + codigoReserva));

        EstadoReserva estadoCancelada = statusReservaRepository.findByCodigoEstado(3); // CANCELADA = 3
        reserva.setEstado(estadoCancelada);
        reservaRepository.save(reserva);

        return new ReservaCriadaResDTO(reserva);
    }

    @Override
    @Transactional
    public ReservaCriadaResDTO buscarReserva(String codigoReserva) {
        Reserva reserva = reservaRepository.getByCodigo(codigoReserva)
                .orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada: " + codigoReserva));

        return new ReservaCriadaResDTO(reserva);
    }

    @Transactional
    @Override
    public List<ReservaCriadaResDTO> buscarListaReservasByClienteCodigo(String clienteCodigo) {
        List<Reserva> reservaList = reservaRepository.findAllByCodigoCliente(clienteCodigo);
        List<ReservaCriadaResDTO> listaReservasDTO = new ArrayList<>();

        reservaList.forEach(a -> listaReservasDTO.add(new ReservaCriadaResDTO(a)));

        return listaReservasDTO;
    }


    @Override
    @Transactional
    public ReservaCriadaResDTO atualizarEstado(String identifier, String estado) throws JsonProcessingException {
        int codigoEstado = mapearEstadoParaCodigo(estado);

        Reserva reserva =  reservaRepository.getByCodigo(identifier).orElseThrow(() -> new ReservaNaoEncontradoException("Reserva não encontrada para código: " + identifier));

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

//        Command commandMessage = new Command(historico);
//        String message = objectMapper.writeValueAsString(commandMessage);
//        rabbitTemplate.convertAndSend("ReservaQueryRequestChannel", message);

        return new ReservaCriadaResDTO(reserva);
    }


    @Transactional
    @Override
    public Boolean atualizarEstadoByCodigoVoo(String codigoVoo, String estado) throws JsonProcessingException {
        try {
            int codigoEstado = mapearEstadoParaCodigo(estado);

            List<Reserva> listReservas = reservaRepository.findAllByCodigoVoo(codigoVoo);

            listReservas.forEach(a -> {
                EstadoReserva estadoFinal = statusReservaRepository.findByCodigoEstado(codigoEstado);

                if (estadoFinal.getDescricaoEstado().equals("REALIZADA")) {
                    if (a.getEstado().getDescricaoEstado().equals("EMBARCADA")) {
                        a.setEstado(estadoFinal);
                    } else {
                        a.setEstado(statusReservaRepository.findByCodigoEstado(8));
                    }

                } else {
                    a.setEstado(estadoFinal);
                }



                reservaRepository.save(a);
            });

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int mapearEstadoParaCodigo(String estado) {
        switch (estado.toUpperCase()) {
            case "CONFIRMADA": return 1;
            case "CHECK-IN":   return 2;
            case "CANCELADA":  return 3;
            case "EMBARCADA":  return 4;
            case "REALIZADA":  return 5;
            case "CRIADA":      return 6;
            case "CANCELADA VOO": return 7;
            case "NÃO REALIZADA": return 8;
            default:
                throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }

    // ✅ Gera código de reserva com 3 letras aleatórias + 3 números (ex: ABC123)
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

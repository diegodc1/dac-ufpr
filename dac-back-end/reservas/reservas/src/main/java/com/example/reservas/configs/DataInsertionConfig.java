package com.example.reservas.configs;

import com.example.reservas.entity.AeroportoEntity;
import com.example.reservas.entity.EstadoReservaEntity;
import com.example.reservas.entity.ReservaEntity;
import com.example.reservas.entity.HistoricoEstatusEntity;
import com.example.reservas.repositorys.AeroportoRepository;
import com.example.reservas.repositorys.EstadoReservaRepository;
import com.example.reservas.repositorys.HistoricoStatusRepository;
import com.example.reservas.repositorys.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataInsertionConfig {

    @Autowired
    private EstadoReservaRepository estadoReservaRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Bean
    public CommandLineRunner dataInserter() {
        return args -> {
            // Inserir estados de reserva, apenas se não existirem
            if (estadoReservaRepository.count() == 0) {
                EstadoReservaEntity estadoConfirmada = new EstadoReservaEntity(1, "CFD", "CONFIRMADA");
                EstadoReservaEntity estadoCheckIn = new EstadoReservaEntity(2, "CKN", "CHECK-IN");
                EstadoReservaEntity estadoCancelada = new EstadoReservaEntity(3, "CLD", "CANCELADA");
                EstadoReservaEntity estadoEmbarcada = new EstadoReservaEntity(4, "EBD", "EMBARCADA");
                EstadoReservaEntity estadoRealizada = new EstadoReservaEntity(5, "RZD", "REALIZADA");

                estadoReservaRepository.saveAll(List.of(estadoConfirmada, estadoCheckIn, estadoCancelada, estadoEmbarcada, estadoRealizada));
                System.out.println("Estados de reserva inseridos no banco.");
            }

            // Inserir aeroportos, apenas se não existirem
            if (aeroportoRepository.count() == 0) {
                AeroportoEntity gru = new AeroportoEntity("GRU", "Aeroporto Internacional de Guarulhos", "São Paulo", "SP");
                AeroportoEntity gig = new AeroportoEntity("GIG", "Aeroporto Internacional do Galeão", "Rio de Janeiro", "RJ");

                aeroportoRepository.save(gru); // Usando save para persistir
                aeroportoRepository.save(gig); // Usando save para persistir
                System.out.println("Aeroportos inseridos no banco.");
            }

            // Inserir reserva (exemplo) com ZonedDateTime
            ZonedDateTime dataReserva = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
            AeroportoEntity gru = aeroportoRepository.findByCodigo("GRU").orElseThrow(() -> new RuntimeException("Aeroporto GRU não encontrado"));
            AeroportoEntity gig = aeroportoRepository.findByCodigo("GIG").orElseThrow(() -> new RuntimeException("Aeroporto GIG não encontrado"));
            EstadoReservaEntity estadoConfirmada = estadoReservaRepository.findByDescricao("CONFIRMADA")
                    .orElseThrow(() -> new RuntimeException("Estado 'CONFIRMADA' não encontrado"));

            ReservaEntity reserva = new ReservaEntity(
                    "ABC123", // Número da reserva
                    "TADS0001", // Código do voo ou identificador adicional
                    dataReserva,  // Data e hora da reserva com fuso horário
                    BigDecimal.valueOf(250.00),  // Valor da reserva
                    50,  // Quantidade de passageiros
                    1,  // Quantidade de bagagens (exemplo)
                    123456,  // Código do cliente
                    UUID.randomUUID(),  // Gerando UUID para transação
                    gru,  // Aeroporto de origem
                    gig,  // Aeroporto de destino
                    estadoConfirmada  // Estado inicial da reserva
            );

            // Usar merge() para garantir que a reserva seja atualizada caso já exista no banco
            reservaRepository.save(reserva);  // O save já irá lidar com a persistência
            System.out.println("Reserva inserida ou atualizada no banco.");

            // Inserir histórico de status
            HistoricoEstatusEntity historico = new HistoricoEstatusEntity(reserva, estadoConfirmada, estadoConfirmada);
            historicoStatusRepository.save(historico);
            System.out.println("Histórico de status inserido no banco.");
        };
    }
}

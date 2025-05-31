package com.example.reservas.sagas.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Comando para criação de uma nova reserva.
 * Utilizado no contexto de CQRS e SAGA para iniciar o processo de reserva.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriarReserva {

    // O código da reserva será gerado no backend (não precisa ser enviado pelo cliente)
    private UUID codigoReserva;

    // Código do voo relacionado à reserva
    private String codigoVoo;

    // Valor total da reserva
    private BigDecimal valor;

    // Quantidade de milhas utilizadas na reserva
    private Integer milhasUtilizadas;

    // Total de assentos reservados (ou poltronas)
    private Integer quantidadePoltronas;

    // Identificador do cliente que está fazendo a reserva
    private String codigoCliente;
    private String codigoAeroportoOrigem;
    
    private String codigoAeroportoDestino;


    // ID da transação (usado para rastreamento em sagas)
    private UUID idTransacao;

    // Tipo da mensagem (ex: "CriarReserva") - útil para identificação em brokers como RabbitMQ
    private String messageType;
}

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

    // O código da reserva será gerado no backend
    private UUID codigoReserva;

    // Código do voo relacionado à reserva
    private String codigo_voo;

    // Valor total da reserva
    private BigDecimal valor;

    // Quantidade de milhas utilizadas na reserva
    private Integer milhas_utilizadas;

    // Total de assentos reservados (ou poltronas)
    private Integer quantidade_poltronas;

    // Identificador do cliente que está fazendo a reserva
    private String codigo_cliente;
    private String codigo_aeroporto_origem;
    
    private String codigo_aeroporto_destino;


    // ID da transação (usado para rastreamento em sagas)
    private UUID idTransacao;

    // Tipo da mensagem (ex: "CriarReserva") - útil para identificação em brokers como RabbitMQ
    private String messageType;
}

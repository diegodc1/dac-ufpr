package com.example.reservas.sagas.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriarReserva {

  
    @JsonProperty("codigo_reserva")
    private UUID codigoReserva;

   
    @JsonProperty("codigo_voo")
    private String codigoVoo;

  
    @JsonProperty("valor")
    private BigDecimal valor;

    
    @JsonProperty("milhas_utilizadas")
    private Integer milhasUtilizadas;

    @JsonProperty("quantidade_poltronas")
    private Integer quantidadePoltronas;

    
    @JsonProperty("codigo_cliente")
    private Integer codigoCliente;

    @JsonProperty("codigo_aeroporto_origem")
    private String codigoAeroportoOrigem;

    @JsonProperty("codigo_aeroporto_destino")
    private String codigoAeroportoDestino;

    // ID da transação (usado para rastreamento em sagas)
    @JsonProperty("id_transacao")
    private UUID idTransacao;

    // Tipo da mensagem (ex: "CriarReserva") - útil para identificação em brokers como RabbitMQ
    @JsonProperty("message_type")
    private String messageType;

   
    @JsonProperty("estado")
    private String estado;

}

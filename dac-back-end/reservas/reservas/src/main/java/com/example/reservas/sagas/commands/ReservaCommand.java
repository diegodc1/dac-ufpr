package com.example.reservas.sagas.commands;

import java.math.BigDecimal;
//import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.example.reservas.entity.ReservaEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaCommand {
    private UUID idReservaCommand;
    private String codigoReserva;
    private String codigoVoo;
    private ZonedDateTime data;

    // Status da reserva (substituindo 'estado' por 'descricao')
    private UUID idEstadoCommand;
    private Integer codigoEstado;
    private String acronimoEstado;
    private String descricaoEstado;

    // Detalhes da reserva
    private BigDecimal valor;
    private Integer milhasUtilizadas;
    private Integer quantidadePoltronas;
    private Integer codigoCliente;  // Alterei para String, pois no código da ReservaEntity é String
    private UUID idTransacao;
    private String messageType;

    // Construtor que inicializa a partir de uma ReservaEntity
    public ReservaCommand(ReservaEntity reserva) {
        // Conversão de UUID para idReservaCommand
        idReservaCommand = reserva.getIdReserva(); // Já é UUID, então não precisa de conversão extra

        codigoReserva = reserva.getCodigo();
        codigoVoo = reserva.getCodigoVoo();
        data = reserva.getData();
        
        // Estado da reserva (agora referenciado como 'descricao')
        idEstadoCommand = reserva.getDescricao().getIdEstado();  // Utilizando 'descricao' para acessar o estado
        codigoEstado = reserva.getDescricao().getCodigoEstado(); // Usando 'descricao' para acessar o código do estado
        acronimoEstado = reserva.getDescricao().getAcronimoEstado(); // Acessando o acrônimo do estado
        descricaoEstado = reserva.getDescricao().getDescricao();  // Acessando a descrição do estado
        
        // Detalhes da reserva
        valor = reserva.getValor();
        milhasUtilizadas = reserva.getMilhasUtilizadas();
        quantidadePoltronas = reserva.getQuantidadePoltronas();
        codigoCliente = reserva.getCodigoCliente();  // Atribuindo o código do cliente
        idTransacao = reserva.getIdTransacao(); // Certificando que o idTransacao é UUID
        messageType = "ReservaCommand";
    }
}

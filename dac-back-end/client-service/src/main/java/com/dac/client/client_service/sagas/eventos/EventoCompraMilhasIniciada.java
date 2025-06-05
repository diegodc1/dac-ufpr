package com.dac.client.client_service.sagas.eventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoCompraMilhasIniciada {
    private String mensagem = "EventoCompraMilhasIniciada";
    private String emailCliente;
    private int quantidadeMilhas;
    private double valorPago;
}

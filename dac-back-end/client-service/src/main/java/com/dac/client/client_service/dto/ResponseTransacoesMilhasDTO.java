package com.dac.client.client_service.dto;

import com.dac.client.client_service.model.TransacaoMilhas;

import java.util.List;

public record ResponseTransacoesMilhasDTO(Long codigo, Integer saldo_milhas, List<TransacoesMilhasPTDTO> transacoes) {
}

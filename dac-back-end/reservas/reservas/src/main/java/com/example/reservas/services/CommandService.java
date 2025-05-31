package com.example.reservas.services;

import com.example.reservas.dto.CheckinDTO;
import com.example.reservas.sagas.commands.CriarReserva;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommandService {
    String criarReserva(CriarReserva command);
    void cancelarReserva(String codigoReserva);
    CheckinDTO atualizarEstadoReserva(String identifier, String estado) throws JsonProcessingException;
}

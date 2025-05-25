package com.example.reservas.services;




import com.example.reservas.dto.CheckinDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CommandService {

    //  R12...
    CheckinDTO updateStatusReserva(String identifier, int statusCode) throws JsonProcessingException;

}

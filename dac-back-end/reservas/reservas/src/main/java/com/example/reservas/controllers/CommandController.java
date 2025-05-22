package com.example.reservas.controllers;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservas.dto.CheckinDTO;
import com.example.reservas.services.CommandService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@CrossOrigin
@RequestMapping("reserva-command")
public class CommandController {

    @Autowired
    private CommandService commandService;

   

  
    @PutMapping("/check-in/{id}")
    public ResponseEntity<CheckinDTO> doCheckIn(@PathVariable(value = "id") UUID bookingId) throws JsonProcessingException {
        CheckinDTO dto = commandService.updateStatusReserva(bookingId.toString(), 2); // 2 é o código para check-in
        return ResponseEntity.ok().body(dto);
    }

    
    @PutMapping("/embarque-passageiro/{cod}")
    public ResponseEntity<CheckinDTO> embarquePassageiro(@PathVariable(value = "cod") String codReserva)
            throws JsonProcessingException {
        CheckinDTO dto =  commandService.updateStatusReserva(codReserva, 4); 
        return ResponseEntity.ok().body(dto);
    }
}

package com.example.reservas.controllers;

import com.example.reservas.dto.EstadoReservaDTO;
import com.example.reservas.sagas.commands.CriarReserva;
import com.example.reservas.services.CommandService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CommandController {

    @Autowired
    private CommandService commandService;

   
     // Cria uma nova reserva.
     
    @PostMapping("reservas")
    public ResponseEntity<?> criarReserva(@RequestBody CriarReserva command) {
        try {
            String codigoReserva = commandService.criarReserva(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(codigoReserva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de conflito: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    
    //Cancela uma reserva existente.
     

    @DeleteMapping("reservas/{codigoReserva}")
    public ResponseEntity<?> cancelarReserva(@PathVariable UUID codigoReserva) {
        try {
            commandService.cancelarReserva(codigoReserva.toString());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de reserva inválido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cancelar reserva.");
        }
    }


     // Atualiza o estado da reserva.
     
    @PatchMapping("reservas/{codigoReserva}/estado")
    public ResponseEntity<?> atualizarEstadoReserva(
            @PathVariable UUID codigoReserva,
            @RequestBody EstadoReservaDTO dto) {

        try {
            commandService.atualizarEstado(codigoReserva.toString(), dto.getEstado());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos: " + e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro no processamento do JSON.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar estado.");
        }
    }
}

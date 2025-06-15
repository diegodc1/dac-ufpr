package com.example.reservas.controllers;

import com.example.reservas.dto.EstadoReservaDTO;
import com.example.reservas.exceptions.ReservaNaoEncontradoException;
import com.example.reservas.sagas.commands.CriarReserva;
import com.example.reservas.services.CommandService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*") // ou configure o domínio correto
public class CommandController {

    @Autowired
    private CommandService commandService;

    /**
     * Cria uma nova reserva.
     * @param command objeto contendo dados para criação da reserva
     * @return código da reserva criada ou erro apropriado
     */
  @PostMapping
public ResponseEntity<?> criarReserva(@RequestBody CriarReserva command) {
    try {
        String codigoReserva = commandService.criarReserva(command);
        Map<String, String> response = Map.of("codigo", codigoReserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Dados inválidos: " + e.getMessage());
    } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Erro de conflito: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro inesperado: " + e.getMessage());
    }
}


    /**
     * Cancela uma reserva existente pelo código UUID.
     * @param codigoReserva UUID da reserva a cancelar
     * @return 204 No Content ou erro
     */
     @DeleteMapping("/{codigoReserva}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable String codigoReserva) {
        try {
            commandService.cancelarReserva(codigoReserva);
            return ResponseEntity.ok().build();
        } catch (ReservaNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @PatchMapping("/{codigoReserva}/estado")
    public ResponseEntity<?> atualizarEstadoReserva(
            @PathVariable String codigoReserva,
            @RequestBody EstadoReservaDTO dto) {

        try {
            commandService.atualizarEstado(codigoReserva.toString(), dto.getEstado());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos: " + e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro no processamento do JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar estado.");
        }

        
    }

    
}


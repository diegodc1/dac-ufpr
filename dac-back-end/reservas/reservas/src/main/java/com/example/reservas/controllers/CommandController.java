package com.example.reservas.controllers;

import com.example.reservas.dto.EstadoReservaDTO;
import com.example.reservas.sagas.commands.CriarReserva;
import com.example.reservas.services.CommandService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CommandController {

    @Autowired
    private CommandService commandService;

    
     /* Cria uma nova reserva.
     */
    @PostMapping("reservas")
    public ResponseEntity<String> criarReserva(@RequestBody CriarReserva command) {
        String codigoReserva = commandService.criarReserva(command);
        return ResponseEntity.ok(codigoReserva);
    }

    /* Cancela uma reserva existente.
     */
    @DeleteMapping("reservas/{codigoReserva}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable UUID codigoReserva) {
        commandService.cancelarReserva(codigoReserva.toString());
        return ResponseEntity.noContent().build();
    }

    
    /* Atualiza o estado da reserva (ex: CHECK-IN, EMBARCADA, CANCELADA).
 */
@PatchMapping("reservas/{codigoReserva}/estado")
public ResponseEntity<Void> atualizarEstadoReserva(
        @PathVariable UUID codigoReserva,
        @RequestBody EstadoReservaDTO dto) throws JsonProcessingException {

    commandService.atualizarEstadoReserva(codigoReserva.toString(), dto.getEstado());
    return ResponseEntity.noContent().build();
}

}

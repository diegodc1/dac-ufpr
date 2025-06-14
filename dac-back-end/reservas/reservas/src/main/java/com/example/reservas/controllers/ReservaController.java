/*package com.example.reservas.controllers;

import com.example.reservas.dto.ReservaDTO;
import com.example.reservas.services.ReservaService;
//import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Criar nova reserva
    @PostMapping
    public ResponseEntity<ReservaDTO> criarReserva( @RequestBody ReservaDTO dto) {
        ReservaDTO resposta = reservaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    // Buscar reserva pelo código
    @GetMapping("/{codigo}")
    public ResponseEntity<ReservaDTO> buscar(@PathVariable String codigo) {
        ReservaDTO dto = reservaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(dto);
    }

    // Atualizar estado da reserva (patch)
    @PatchMapping("/{codigo}/estado")
    public ResponseEntity<ReservaDTO> atualizarEstado(@PathVariable String codigo, @RequestBody Map<String, String> estadoBody) {
        String novoEstado = estadoBody.get("estado");
        if (novoEstado == null || novoEstado.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        ReservaDTO dto = reservaService.atualizarEstado(codigo, novoEstado);
        return ResponseEntity.ok(dto);
    }

    // Cancelar reserva (delete)
    @DeleteMapping("/{codigo}")
    public ResponseEntity<ReservaDTO> cancelar(@PathVariable String codigo) {
        ReservaDTO dto = reservaService.cancelar(codigo);
        return ResponseEntity.ok(dto);
    }
}
*/
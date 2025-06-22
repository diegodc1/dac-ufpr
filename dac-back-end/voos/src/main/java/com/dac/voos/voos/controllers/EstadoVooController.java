package com.dac.voos.voos.controllers;

import com.dac.voos.voos.dto.EstadoVooDTO;
import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.services.EstadoVooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/estadoVoo")
public class EstadoVooController {

    @Autowired
    private EstadoVooService estadoVooService;

    @PostMapping("/newEstadoVoo")
    public ResponseEntity<String> newEstadoVoo(@RequestBody EstadoVooDTO estadoVooDTO){
        estadoVooService.saveEstadoVoo(
                estadoVooDTO.sigla(),
                estadoVooDTO.descricao()
        );
        return ResponseEntity.ok("criado");
    }

    @GetMapping("/todosEstadoVoo")
    public ResponseEntity<List<EstadoVoo>> todosEstadoVoo(){
        List<EstadoVoo> estadoVoos = estadoVooService.listEstadoVoo();
        return ResponseEntity.ok(estadoVoos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoVoo> buscarPorId(@PathVariable Long id){
        Optional<EstadoVoo> estadoVoo = estadoVooService.listEstadoVooId(id);
        return estadoVoo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerEstadoVoo(
            @PathVariable Long id){
        try {
            estadoVooService.removerEstadoVoo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Erro ao remover");
        }
    }
}

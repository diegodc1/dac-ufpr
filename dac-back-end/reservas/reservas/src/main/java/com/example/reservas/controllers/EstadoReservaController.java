package com.example.reservas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservas.dto.EstadoReservaDTO;
import com.example.reservas.services.EstadoReservaService;

@RestController
@CrossOrigin
@RequestMapping("/api/estados")
public class EstadoReservaController {

    @Autowired
    private EstadoReservaService estadoReservaService;

    
    @GetMapping(value = "")
    public <estadoReservaService> List<EstadoReservaDTO> listarEstados() {
        return estadoReservaService.listarEstados();
    }

    
    @GetMapping("/{id}")
    public EstadoReservaDTO obterEstado(@PathVariable Long id) {
        return estadoReservaService.obterEstadoPorId(id);
    }
}



package com.example.reservas.controllers;

import com.example.reservas.dto.ReservaDTO;
import com.example.reservas.services.ReservaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

  
    @PostMapping
    public ReservaDTO criarReserva(@RequestBody ReservaDTO reservaDTO) {
        return reservaService.criarReserva(reservaDTO);
    }

    
    @GetMapping("")
    public List<ReservaDTO> listarReservas() {
        return reservaService.listarReservas();
    }

   
    @GetMapping("/{id}")
    public ReservaDTO obterReserva(@PathVariable Long id) {
        return reservaService.obterReservaPorId(id);
    }
}

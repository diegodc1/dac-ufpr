package com.example.reservas.controllers;

import com.example.reservas.dto.HistoricoReservaDTO;
import com.example.reservas.services.HistoricoReservaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/historicos")
public class HistoricoReservaController {

    @Autowired
    private HistoricoReservaService historicoReservaService;


    @PostMapping
    public HistoricoReservaDTO criarHistorico(@RequestBody HistoricoReservaDTO historicoReservaDTO) {
        return historicoReservaService.criarHistorico(historicoReservaDTO);
    }

    
    @GetMapping("/reserva/{reservaId}")
    public List<HistoricoReservaDTO> listarHistoricoPorReserva(@PathVariable Long reservaId) {
        return historicoReservaService.listarHistoricoPorReserva(reservaId);
    }
}

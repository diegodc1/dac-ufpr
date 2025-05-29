package com.topus.reservas_query_service.controllers;




import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.topus.reservas_query_service.dto.response.R03ResDTO;
import com.topus.reservas_query_service.dto.response.R04ResDTO;
import com.topus.reservas_query_service.dto.response.R09ResDTO;
import com.topus.reservas_query_service.services.ReservaQueryService;

@RestController
@CrossOrigin
@RequestMapping("reserva-query")
public class ReservasQueryController {

    @Autowired
    private ReservaQueryService reservaQueryService;

    // R03 - 2
    @GetMapping("/reserva-cliente/{id}")
    public ResponseEntity<List<R03ResDTO>> getClientBookings(@PathVariable(value = "id") String idUsuario) {
        System.out.println("CHEGOU" + idUsuario);
        List<R03ResDTO> dto = reservaQueryService.findReservasCliente(idUsuario);
        return ResponseEntity.ok().body(dto);
    }

    // R04
    @GetMapping("/reserva/{id}")
    public ResponseEntity<R04ResDTO> getReserva(@PathVariable(value = "id") String idReserva) {
        R04ResDTO dto = reservaQueryService.getReserva(idReserva);
        return ResponseEntity.ok().body(dto);
    }

    // R06 - 2
    @GetMapping("/cods-voo")
    public ResponseEntity<List<String>> getFlightCodes(@RequestParam List<UUID> idsTransacao) {
        List<String> dto = reservaQueryService.getCodsVoo(idsTransacao);
        return ResponseEntity.ok().body(dto);
    }

    // R09 - 1
    @GetMapping("/busca-reserva/{cod}")
    public ResponseEntity<R09ResDTO> searchReserva(@PathVariable(value = "cod") String codReserva) {
        R09ResDTO dto = reservaQueryService.searchReserva(codReserva);
        return ResponseEntity.ok().body(dto);
    }
}

package com.example.reservas.services;

import com.example.reservas.dto.EstadoReservaDTO;
import com.example.reservas.enums.EstadoReserva;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstadoReservaService {

  
    private static List<EstadoReservaDTO> estadoReservaList = new ArrayList<>();

    static {
        
        estadoReservaList.add(new EstadoReservaDTO(1L, EstadoReserva.CRIADO));
        estadoReservaList.add(new EstadoReservaDTO(2L, EstadoReserva.CHECK_IN));
        estadoReservaList.add(new EstadoReservaDTO(3L, EstadoReserva.CANCELADO));
    }

  
    public EstadoReservaDTO obterEstadoPorId(Long id) {
      
        Optional<EstadoReservaDTO> estadoReserva = estadoReservaList.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();

        return estadoReserva.orElseThrow(() -> new RuntimeException("Estado não encontrado para o ID " + id));
    }

    
    public List<EstadoReservaDTO> listarEstados() {
        return estadoReservaList;
    }

    
    public EstadoReservaDTO adicionarEstado(Long id, EstadoReserva estado) {
        EstadoReservaDTO novoEstado = new EstadoReservaDTO(id, estado);
        estadoReservaList.add(novoEstado);
        return novoEstado;
    }

    
    public EstadoReservaDTO atualizarEstado(Long id, EstadoReserva estado) {
        EstadoReservaDTO estadoReserva = obterEstadoPorId(id);
        estadoReserva.setEstado(estado);
        return estadoReserva;
    }
}

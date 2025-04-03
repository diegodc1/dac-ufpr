package com.example.reservas.dto;

import com.example.reservas.enums.EstadoReserva;

public class EstadoReservaDTO {

    private Long id;
    private EstadoReserva estado;

 
    public EstadoReservaDTO(Long id, EstadoReserva estado) {
        this.id = id;
        this.estado = estado;
    }

  
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
}

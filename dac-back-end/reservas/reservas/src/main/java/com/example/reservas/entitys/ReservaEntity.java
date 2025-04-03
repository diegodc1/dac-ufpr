package com.example.reservas.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    private String codigoVoo;  
    private String codigoReserva; 
    private LocalDateTime dataHoraReserva;  
    
    public ReservaEntity() {
    }

    
    public ReservaEntity(String codigoVoo, String codigoReserva, LocalDateTime dataHoraReserva) {
        this.codigoVoo = codigoVoo;
        this.codigoReserva = codigoReserva;
        this.dataHoraReserva = dataHoraReserva;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(String codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public LocalDateTime getDataHoraReserva() {
        return dataHoraReserva;
    }

    public void setDataHoraReserva(LocalDateTime dataHoraReserva) {
        this.dataHoraReserva = dataHoraReserva;
    }

}

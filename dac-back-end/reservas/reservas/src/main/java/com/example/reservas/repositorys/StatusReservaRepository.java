package com.example.reservas.repositorys;

import com.example.reservas.model.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusReservaRepository extends JpaRepository<EstadoReserva, UUID> {

    
    EstadoReserva findByCodigoEstado(Integer codigoEstado);
}

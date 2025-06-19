package com.example.reservas.repositorys;

import com.example.reservas.model.EstadoReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusReservaRepository extends JpaRepository<EstadoReservaModel, UUID> {

    
    EstadoReservaModel findByCodigoEstado(Integer codigoEstado);
}

package com.example.reservas.repositorys;

import com.example.reservas.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusReservaRepository extends JpaRepository<StatusReserva, UUID> {

    // Corrigido: o nome do campo é "codStatus", não "statusCode"
    StatusReserva findByCodStatus(Integer codStatus);
}

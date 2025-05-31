package com.example.reservas.repositorys;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.reservas.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    @Query("select r from Reserva r where r.codigoReserva = ?1")
    Optional<Reserva> getByCodigoReserva(String codigoReserva);

    
    List<Reserva> findByCodigoVoo(String codigoVoo);

}
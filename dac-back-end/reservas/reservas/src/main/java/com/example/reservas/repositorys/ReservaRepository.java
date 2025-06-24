package com.example.reservas.repositorys;

import com.example.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    // Buscar reserva por código
    @Query("select r from Reserva r where r.codigo = ?1")
    Optional<Reserva> getByCodigo(String codigo);
    
    // Buscar reservas por código de voo
    List<Reserva> findByCodigoVoo(String codigoVoo);

    // Buscar reservas por código do aeroporto de origem
    @Query("select r from Reserva r where r.aeroportoOrigem.codigo = ?1")
    List<Reserva> findByAeroportoOrigemCodigo(String codigoAeroportoOrigem);

    // Buscar reservas por código do aeroporto de destino
    @Query("select r from Reserva r where r.aeroportoDestino.codigo = ?1")
    List<Reserva> findByAeroportoDestinoCodigo(String codigoAeroportoDestino);
}

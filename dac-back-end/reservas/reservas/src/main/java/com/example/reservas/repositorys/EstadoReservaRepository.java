package com.example.reservas.repositorys;

import com.example.reservas.entity.EstadoReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstadoReservaRepository extends JpaRepository<EstadoReservaEntity, UUID> {

    Optional<EstadoReservaEntity> findByCodigoEstado(int codigoEstado);

   
    Optional<EstadoReservaEntity> findByDescricaoEstado(String descricaoEstado);
}

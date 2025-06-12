package com.example.reservas.repositorys;

import com.example.reservas.entity.ReservaEntity;
//import com.example.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, UUID> {

    Optional<ReservaEntity> findByCodigo(String codigo);

    List<ReservaEntity> findByCodigoVoo(String codigoVoo);

}

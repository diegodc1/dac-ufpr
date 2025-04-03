package com.example.reservas.repositorys;

import com.example.reservas.entitys.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    
    List<ReservaEntity> findByCodigoVoo(String codigoVoo);

    List<ReservaEntity> findByCodigoReserva(String codigoReserva);

    
}

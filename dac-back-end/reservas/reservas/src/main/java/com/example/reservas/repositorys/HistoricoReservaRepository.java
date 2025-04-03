package com.example.reservas.repositorys;

import com.example.reservas.entitys.HistoricoReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoReservaRepository extends JpaRepository<HistoricoReservaEntity, Long> {

    
    List<HistoricoReservaEntity> findByReservaId(Long reservaId);
}

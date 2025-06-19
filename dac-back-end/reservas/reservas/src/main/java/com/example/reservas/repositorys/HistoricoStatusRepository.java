package com.example.reservas.repositorys;

import com.example.reservas.entity.HistoricoEstatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoEstatusEntity, UUID> {
    
}

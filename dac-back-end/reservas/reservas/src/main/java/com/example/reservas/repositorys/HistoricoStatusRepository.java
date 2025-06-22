package com.example.reservas.repositorys;



import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reservas.model.HistoricoEstatus;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoEstatus, UUID> {

}

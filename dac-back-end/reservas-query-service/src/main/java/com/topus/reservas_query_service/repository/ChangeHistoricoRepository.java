package com.topus.reservas_query_service.repository;



import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.topus.reservas_query_service.model.ChangeHistoricoQuery;

@Repository
public interface ChangeHistoricoRepository extends JpaRepository<ChangeHistoricoQuery , UUID>{

}






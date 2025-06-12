package com.example.reservas.repositorys;

import com.example.reservas.entity.AeroportoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AeroportoRepository extends JpaRepository<AeroportoEntity, String> {
}

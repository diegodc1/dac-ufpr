package com.dac.voos.voos.repositorys;

import com.dac.voos.voos.entitys.EstadoVoo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoVooRepository  extends JpaRepository<EstadoVoo, Long> {
    Optional<EstadoVoo> findBySigla(String ca);
}

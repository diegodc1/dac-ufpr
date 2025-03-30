package com.dac.voos.voos.repositorys;

import com.dac.voos.voos.entitys.EstadoVoo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoVooRepository  extends JpaRepository<EstadoVoo, Long> {
}

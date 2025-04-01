package com.dac.voos.voos.repositorys;

import com.dac.voos.voos.entitys.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AeroportoRepository  extends JpaRepository<Aeroporto , String> {
}

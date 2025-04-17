package com.dac.voos.voos.repositorys;

import com.dac.voos.voos.entitys.Voos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VooRepository extends JpaRepository<Voos, Long> {
}

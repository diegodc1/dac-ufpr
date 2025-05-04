package com.dac.voos.voos.repositorys;

import com.dac.voos.voos.entitys.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AeroportoRepository  extends JpaRepository<Aeroporto , String> {
    @Query("SELECT a FROM Aeroporto a WHERE a.codigo = :codigo")
    Optional<Aeroporto> findByCodigo(@Param("codigo") String codigo);}

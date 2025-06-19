package com.example.reservas.repositorys;

import com.example.reservas.entity.AeroportoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AeroportoRepository extends JpaRepository<AeroportoEntity, String> {

    // Buscar aeroporto por nome (case-insensitive)
    List<AeroportoEntity> findByNomeIgnoreCase(String nome);

    // Buscar aeroporto por cidade
    List<AeroportoEntity> findByCidade(String cidade);

    // Buscar aeroporto por estado (UF)
    List<AeroportoEntity> findByUf(String uf);

    // Buscar aeroporto pelo código IATA (código único)
    Optional<AeroportoEntity> findByCodigo(String codigo);

    // Buscar aeroporto por nome e cidade (combinado)
    List<AeroportoEntity> findByNomeIgnoreCaseAndCidade(String nome, String cidade);
}

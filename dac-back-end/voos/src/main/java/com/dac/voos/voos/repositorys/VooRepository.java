package com.dac.voos.voos.repositorys;

import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.Voos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface VooRepository extends JpaRepository<Voos, Long> {

    @Query("SELECT v FROM Voos v WHERE v.data_hora BETWEEN :dataInicio AND :dataFim AND v.aeroportoOrigem = :origem AND v.aeroportoDestino = :destino")
    List<Voos> buscarPorDataHoraOrigemDestino(
            @Param("dataInicio") OffsetDateTime dataInicio,
            @Param("dataFim") OffsetDateTime dataFim,
            @Param("origem") Aeroporto origem,
            @Param("destino") Aeroporto destino
    );
    @Query("SELECT v FROM Voos v WHERE v.data_hora BETWEEN :dataInicio AND :dataFim ORDER BY v.data_hora")
    List<Voos> buscarVoosPorIntervaloDeDatas(
            //  @Param("dataInicio") LocalDateTime dataInicio,
            //@Param("dataFim") LocalDateTime dataFim
            @Param("dataInicio") OffsetDateTime dataInicio,
            @Param("dataFim") OffsetDateTime dataFim
    );
}

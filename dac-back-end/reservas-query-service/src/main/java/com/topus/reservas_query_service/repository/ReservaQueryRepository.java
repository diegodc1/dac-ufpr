package com.topus.reservas_query_service.repository;




import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.topus.reservas_query_service.model.ReservaQuery;

@Repository
public interface ReservaQueryRepository extends JpaRepository<ReservaQuery, UUID> {

    Optional<List<ReservaQuery>> findByIdUsuario(String idUsuario);

    ReservaQuery findByIdReservaCommand(UUID idReservaCommand);

    Optional<ReservaQuery> findByIdTransacao(UUID idTransaction);

    Optional<ReservaQuery> findByCodVoo(String codVoo);
}

package com.topus.reservas_query_service.sagas;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topus.reservas_query_service.model.ReservaQuery;
import com.topus.reservas_query_service.repository.ReservaQueryRepository;
import com.topus.reservas_query_service.sagas.command.ReservaCommand;

@Service
public class SagaService {

    @Autowired
    private ReservaQueryRepository reservaQueryRepository;

    public void insertReserva(ReservaCommand reservaCommand) {

        ReservaQuery newReserva = new ReservaQuery(reservaCommand);

        reservaQueryRepository.save(newReserva);

    }

}

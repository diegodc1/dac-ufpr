package com.topus.reservas_query_service.services;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topus.reservas_query_service.cqrs.commands.Command;
import com.topus.reservas_query_service.dto.response.R03ResDTO;
import com.topus.reservas_query_service.dto.response.R04ResDTO;
import com.topus.reservas_query_service.dto.response.R09ResDTO;
import com.topus.reservas_query_service.exceptions.ReservaNaoEncontradaExeption;
import com.topus.reservas_query_service.exceptions.UsuarioReservaNaoEncontrado;
import com.topus.reservas_query_service.model.ReservaQuery;
import com.topus.reservas_query_service.model.ChangeHistoricoQuery;
import com.topus.reservas_query_service.repository.ReservaQueryRepository;
import com.topus.reservas_query_service.repository.ChangeHistoricoRepository;

@Service
public class ReservaQueryServiceImp implements ReservaQueryService {

    @Autowired
    private ReservaQueryRepository reservaQueryRepository;

    @Autowired
    private ChangeHistoricoRepository changeHistoricoRepository;

    ReservaQueryServiceImp(ChangeHistoricoRepository changeHistoricoRepository) {
        this.changeHistoricoRepository = changeHistoricoRepository;
    }

    // R03 - 2
    @Override
    public List<R03ResDTO> findReservasCliente(String idUsuario) {

        System.out.println("CHEGOU" + idUsuario);
        List<ReservaQuery> reservasCliente = reservaQueryRepository.findByIdUsuario(idUsuario)
                .filter(reservas -> !reservas.isEmpty()) // Verifica se a lista nao esta vazia
                .orElseThrow(() -> new UsuarioReservaNaoEncontrado("Reservas não encontrada para usuario: " + idUsuario));

        return reservasCliente.stream().map(R03ResDTO::new).toList();
    }

    // R04
    @Override
    public R04ResDTO getReserva(String idReserva) {

        ReservaQuery reserva = reservaQueryRepository.findById(UUID.fromString(idReserva)).orElseThrow(

                () -> new ReservaNaoEncontradaExeption("Reserva não foi encontrado por id: " + idReserva));

        return new R04ResDTO(reserva);
    }

    // R06 - 2
    @Override
    public List<String> getCodsVoo(List<UUID> idsTransacao) {

        List<String> listR06ResDTO = new ArrayList<>();

        for (UUID uuid : idsTransacao) {

            ReservaQuery reservaQuery = reservaQueryRepository.findByIdTransacao(uuid).orElseThrow(null);

            String codVoo = reservaQuery.getCodVoo();

            listR06ResDTO.add(codVoo);

        }

        return listR06ResDTO;
    }


    // R09 - 1
    @Override
    public R09ResDTO searchReserva(String codReserva) {

        ReservaQuery reserva= reservaQueryRepository.findByCodVoo(codReserva).orElseThrow(
                () -> new ReservaNaoEncontradaExeption("Reserva não foi encontrata para esse Codigo: " + codReserva));

        return new R09ResDTO(reserva);
    }

    // R10, R08, R12, R13, R14
    @Override
    public void syncronizeDBs(Command command) {

        // 1. atualiza o status da reserva
        ReservaQuery reservaQuery = reservaQueryRepository.findByIdReservaCommand(command.getIdReserva());

        reservaQuery.setIdStatusCommand(command.getIdFStatusCommand());
        reservaQuery.setCodStatus(command.getCodFStatus());
        reservaQuery.setStatusAcronym(command.getFStatusAcronym());
        reservaQuery.setStatusDescricao(command.getFStatusDescricao());

        reservaQuery = reservaQueryRepository.save(reservaQuery);

        // 2. cria um registro "espelho" da mudança de estado
   ChangeHistoricoQuery changeHistoricoQuery =    ChangeHistoricoQuery.builder()
                .idCommandTroca(command.getIdTroca())
                .dataChange(command.getDataTroca())
                .codReserva(reservaQuery.getCodReserva()) // * reference from the another table
                .idStatusCommandI(command.getIdIStatusCommand())
                .codStatusI(command.getCodIStatus())
                .iStatusAcronym(command.getIStatusAcronym())
                .iStatusDescricao(command.getDescricaoStatusI())
                .idStatusCommandF(command.getIdFStatusCommand())
                .codStatusF(command.getCodFStatus())
                .fStatusAcronym(command.getFStatusAcronym())
                .statusDescricaoF(command.getFStatusDescricao())
                .build();

     changeHistoricoRepository.save(changeHistoricoQuery);

        System.out.println("\nATUALIZAÇÃO FOI BEM SUCEDIDA, VERIFIQUE BANCOS DE DADOS");

    }

}

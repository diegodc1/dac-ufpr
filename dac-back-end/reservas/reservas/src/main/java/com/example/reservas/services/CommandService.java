package com.example.reservas.services;

import com.example.reservas.dto.CheckinDTO;
import com.example.reservas.dto.ReservaCriadaResDTO;
import com.example.reservas.model.Reserva;
import com.example.reservas.sagas.commands.CriarReserva;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface de serviço de comando responsável por manipular ações
 * que alteram o estado do sistema de reservas.
 * Parte do padrão CQRS (Command Query Responsibility Segregation).
 */
public interface CommandService {

    /**
     * Cria uma nova reserva no sistema.
     *
     * @param command Objeto com os dados necessários para criação da reserva.
     * @return Código gerado da reserva.
     */
    ReservaCriadaResDTO criarReserva(CriarReserva command);

    /**
     * Cancela uma reserva existente a partir do seu código.
     *
     * @param codigoReserva Código da reserva (UUID em string).
     */
    ReservaCriadaResDTO cancelarReserva(String codigoReserva);

    ReservaCriadaResDTO buscarReserva(String codigoReserva);

    @Transactional
    List<ReservaCriadaResDTO> buscarListaReservasByClienteCodigo(String clienteCodigo);

    /**
     * Atualiza o estado atual de uma reserva (ex: CHECK-IN, CANCELADO, etc.).
     *
     * @param identifier Pode ser o código da reserva ou UUID.
     * @param estado Novo estado a ser aplicado.
     * @return DTO contendo as informações do estado anterior e atual.
     * @throws JsonProcessingException Caso ocorra falha ao serializar a mensagem.
     */
    ReservaCriadaResDTO atualizarEstado(String identifier, String estado) throws JsonProcessingException;

    @Transactional
    Boolean atualizarEstadoByCodigoVoo(String codigoVoo, String estado) throws JsonProcessingException;
}

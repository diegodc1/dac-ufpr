package com.dac.client.client_service.repository;

import java.util.List;

import com.dac.client.client_service.model.Cliente;
import com.dac.client.client_service.model.TransacaoMilhas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoMilhasRepository extends JpaRepository<TransacaoMilhas, Long> {

    List<TransacaoMilhas> findByCliente(Cliente cliente);

    List<TransacaoMilhas> findByClienteOrderByDataHoraDesc(Cliente cliente);
}
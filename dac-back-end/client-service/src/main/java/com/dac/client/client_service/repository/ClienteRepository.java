package com.dac.client.client_service.repository;

import com.dac.client.client_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmail(String email);
    Cliente findByCodigo(Long codigo);
    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);
}
package com.dac.client.client_service.repository;

import com.dac.client.client_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Cliente findByEmail(String email);
}
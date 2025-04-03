package com.dac.backend.clientservice.repository;

import com.dac.backend.clientservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
}
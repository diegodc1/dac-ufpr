package com.example.reservas.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_status")
public class StatusReserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_status", nullable = false, updatable = false)
    private UUID idStatus;

    @Column(name = "code_status", nullable = false, unique = true)
    private Integer codStatus;

    @Column(name = "acronimo_status", nullable = false, unique = true)
    private String acronimoStatus;

    @Column(name = "desc_status", nullable = false, unique = true)
    private String DescricaoStatus; 
}
 
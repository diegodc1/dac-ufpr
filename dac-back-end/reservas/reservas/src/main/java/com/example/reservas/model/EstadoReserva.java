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
@Table(name = "tb_estado")
public class EstadoReserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_estado", nullable = false, updatable = false)
    private UUID idEstado;

    @Column(name = "codigo_estado", nullable = false, unique = true)
    private Integer codigoEstado;

    @Column(name = "acronimo_estado", nullable = false, unique = true)
    private String acronimoEstado;

    @Column(name = "desc_estado", nullable = false, unique = true)
    private String DescricaoEstado; 
}
 
package com.example.reservas.model;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_estado_reserva")
@JsonSerialize(using = ToStringSerializer.class)
public class EstadoReserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Usando AUTO, o JPA determinará a estratégia
    @Column(name = "id_estado", nullable = false, updatable = false)
    private UUID idEstado;

    @Column(name = "codigo_estado", nullable = false, unique = true)
    private Integer codigoEstado;

    @Column(name = "acronimo_estado", nullable = false, unique = true)
    private String acronimoEstado;

    @Column(name = "descricao_estado", nullable = false, unique = true)  // Ajustado para descricao_estado
    private String descricaoEstado;

    // Método que garante que o idEstado seja gerado automaticamente
    @PrePersist
    private void prePersist() {
        if (idEstado == null) {
            idEstado = UUID.randomUUID();
        }
    }
}

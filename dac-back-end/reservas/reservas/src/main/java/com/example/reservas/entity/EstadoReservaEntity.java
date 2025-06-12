package com.example.reservas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_estado_reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoReservaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_estado", nullable = false, updatable = false)
    private UUID idEstado;

    @Column(name = "descricao", nullable = false, unique = true)
    private String descricao;  // Exemplo: "CONFIRMADO", "CANCELADO", "EMBARCADO", etc.
}

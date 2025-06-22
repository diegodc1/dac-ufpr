package com.example.reservas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a tabela "tb_estado_reserva" no banco de dados.
 */
@Entity

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_estado_reserva")
public class EstadoReservaEntity {

    /**
     * Código único do estado da reserva.
     * Representa a chave primária.
     */
    @Id
    @Column(name = "codigo_estado")
    private String codigoEstado;

    /**
     * Descrição do estado da reserva.
     */
    @Column(name = "desc_estado")
    private String descricaoEstado;

    /**
     * Acrônimo do estado da reserva (ex: "PEND" para pendente).
     */
    @Column(name = "acronimo_estado")
    private String acronimoEstado;

    /**
     * Identificador único do estado.
     * Pode ser usado como chave primária se necessário.
     */
    @Column(name = "id_estado")
    private Long idEstado;

   
}

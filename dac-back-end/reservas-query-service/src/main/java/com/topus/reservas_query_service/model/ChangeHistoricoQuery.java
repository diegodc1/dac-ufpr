package com.topus.reservas_query_service.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
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
@Table(name = "tb_change_query")
public class ChangeHistoricoQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_troca", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "id_command_change", nullable = false)
    private UUID idCommandTroca; // * id in booking command service

    @Column(name = "data_change", nullable = false)
    private ZonedDateTime dataChange;

    @Column(name = "cod_reserva", nullable = false)
    private String codReserva;

    //  status inicial
    @Column(name = "id_status_command_b", nullable = false)
    private UUID idStatusCommandI; // * id in booking command service

    @Column(name = "cod_status_i", nullable = false)
    private Integer codStatusI;

    @Column(name = "i_status_acronym", nullable = false)
    private String iStatusAcronym;

    @Column(name = "i_status_descricao", nullable = false)
    private String iStatusDescricao;

    //  status final
    @Column(name = "id_status_command_f", nullable = false)
    private UUID idStatusCommandF; // * id in booking command service

    @Column(name = "cod_status_f", nullable = false)
    private Integer codStatusF;

    @Column(name = "f_status_acronym", nullable = false)
    private String fStatusAcronym;

    @Column(name = "status_descricao_f", nullable = false)
    private String statusDescricaoF;

}

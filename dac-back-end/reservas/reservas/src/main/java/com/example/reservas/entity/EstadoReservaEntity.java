package com.example.reservas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.util.UUID;

@Entity
public class EstadoReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Usando a geração automática de UUID
    @Column(name = "id_estado") // Garantindo que o campo 'id_estado' seja mapeado corretamente no banco
    private UUID idEstado;  // Alterado de Long para UUID

    private Integer codigoEstado;
    private String acronimoEstado;
    private String descricao;

    // Construtor com parâmetros
    public EstadoReservaEntity(Integer codigoEstado, String acronimoEstado, String descricao) {
        this.codigoEstado = codigoEstado;
        this.acronimoEstado = acronimoEstado;
        this.descricao = descricao;
    }

    // Construtor padrão (sem parâmetros)
    public EstadoReservaEntity() {
    }

    // Getters and Setters
    public UUID getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(UUID idEstado) {
        this.idEstado = idEstado;
    }

    public Integer getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(Integer codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getAcronimoEstado() {
        return acronimoEstado;
    }

    public void setAcronimoEstado(String acronimoEstado) {
        this.acronimoEstado = acronimoEstado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

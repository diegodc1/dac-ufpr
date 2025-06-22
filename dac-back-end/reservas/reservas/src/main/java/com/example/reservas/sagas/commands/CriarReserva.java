package com.example.reservas.sagas.commands;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriarReserva {

    @JsonProperty("codigo_cliente") // Mapeando campo do JSON "codigo_cliente" para o atributo "codigoCliente"
    private Integer codigoCliente;

    @JsonProperty("valor") // Mapeando campo do JSON "valor"
    private BigDecimal valor;

    @JsonProperty("milhas_utilizadas") // Mapeando campo do JSON "milhas_utilizadas"
    private Integer milhasUtilizadas;

    @JsonProperty("quantidade_poltronas") // Mapeando campo do JSON "quantidade_poltronas"
    private Integer quantidadePoltronas;

    @JsonProperty("codigo_voo") // Mapeando campo do JSON "codigo_voo"
    private String codigoVoo;

    @JsonProperty("codigo_aeroporto_origem") // Mapeando campo do JSON "codigo_aeroporto_origem"
    private String codigoAeroportoOrigem;

    @JsonProperty("codigo_aeroporto_destino") // Mapeando campo do JSON "codigo_aeroporto_destino"
    private String codigoAeroportoDestino;

    // Getters e Setters
    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getMilhasUtilizadas() {
        return milhasUtilizadas;
    }

    public void setMilhasUtilizadas(Integer milhasUtilizadas) {
        this.milhasUtilizadas = milhasUtilizadas;
    }

    public Integer getQuantidadePoltronas() {
        return quantidadePoltronas;
    }

    public void setQuantidadePoltronas(Integer quantidadePoltronas) {
        this.quantidadePoltronas = quantidadePoltronas;
    }

    public String getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(String codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public String getCodigoAeroportoOrigem() {
        return codigoAeroportoOrigem;
    }

    public void setCodigoAeroportoOrigem(String codigoAeroportoOrigem) {
        this.codigoAeroportoOrigem = codigoAeroportoOrigem;
    }

    public String getCodigoAeroportoDestino() {
        return codigoAeroportoDestino;
    }

    public void setCodigoAeroportoDestino(String codigoAeroportoDestino) {
        this.codigoAeroportoDestino = codigoAeroportoDestino;
    }
}

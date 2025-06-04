package com.dac.client.client_service.dto;

public class CompraMilhasDTO {
    private int quantidade;
    private double valorPago;

    public CompraMilhasDTO(){

    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorPago() {
        return this.valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
}

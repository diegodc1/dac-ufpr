package com.dac.client.client_service.exception;
public class SaldoMilhasInsuficienteException extends RuntimeException {
    public SaldoMilhasInsuficienteException(String mensagem) {
        super(mensagem);
    }
}


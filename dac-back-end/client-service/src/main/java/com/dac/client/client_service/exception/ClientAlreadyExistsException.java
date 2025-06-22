package com.dac.client.client_service.exception;
public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException() {
        super("O cliente já existe!");
    }
}

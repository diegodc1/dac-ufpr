package com.dac.client.client_service.dto;

public class TransacaoMilhasDTO {
    private  String date;
    private String reservationCode;
    private String amountReais;
    private int miles;
    private String description;
    private String type;

    public TransacaoMilhasDTO(String formattedDate, String reservationCode, String amountReais, int i, String descricao, String tipo) {
    }
}

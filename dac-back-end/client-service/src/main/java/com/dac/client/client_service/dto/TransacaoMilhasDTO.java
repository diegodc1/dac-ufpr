package com.dac.client.client_service.dto;

public class TransacaoMilhasDTO {
    // Campos do DTO
    private String date;
    private String reservationCode;
    private String amountReais;
    private int miles;
    private String description;
    private String type;


    public TransacaoMilhasDTO() {
    }

    public TransacaoMilhasDTO(String formattedDate, String reservationCode, String amountReais, int miles, String description, String type) {
        this.date = formattedDate;
        this.reservationCode = reservationCode;
        this.amountReais = amountReais;
        this.miles = miles;
        this.description = description;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public String getAmountReais() {
        return amountReais;
    }

    public int getMiles() {
        return miles;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}

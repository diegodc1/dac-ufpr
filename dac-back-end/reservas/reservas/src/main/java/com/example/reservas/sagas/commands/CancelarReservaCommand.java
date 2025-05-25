package com.example.reservas.sagas.commands;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelarReservaCommand {
    private String flightCode;
    private String messageType;
}

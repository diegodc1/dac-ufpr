package com.example.reservas.sagas.commands;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompletaReserva{
    private String codVoo;
    private String messageType;
}

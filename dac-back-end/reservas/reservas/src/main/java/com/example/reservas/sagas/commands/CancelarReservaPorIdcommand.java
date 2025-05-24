package com.example.reservas.sagas.commands;




import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelarReservaPorIdcommand  {
    private UUID bookingId;
    private String messageType;
}
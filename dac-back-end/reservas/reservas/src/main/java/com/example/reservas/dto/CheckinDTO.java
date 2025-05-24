package com.example.reservas.dto;





import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckinDTO {
    private UUID idReserva;
    private String codReserva;
    private String statusInicial;
    private String statusAtual;
}

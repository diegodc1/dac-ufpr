package com.example.reservas.sagas.commands;




import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriarReserva  {
    private UUID idVoo;
    private String codVoo;
    private BigDecimal valor;
    private Integer MilhasUsadas;
    private Integer totalAssentos;
    private String idUsuario;
    private UUID idTransacao;
    private String messageType;
}

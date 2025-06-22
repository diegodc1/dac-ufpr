package com.dac.authentication_service.sagas.eventos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoUsuAtu {
    private String nome;
    private String email;
    private String mensagem;
}

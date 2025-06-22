package com.aerolinha.sagas.atualizafuncionariosaga.eventos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoFuncAtu {
    private Boolean prosseguirSAGA;
    private String nome;
    private String email;
    private String mensagem;
}

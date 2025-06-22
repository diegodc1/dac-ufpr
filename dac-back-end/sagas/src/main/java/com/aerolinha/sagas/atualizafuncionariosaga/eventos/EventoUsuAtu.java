package com.aerolinha.sagas.atualizafuncionariosaga.eventos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoUsuAtu {
    private String idUsuario;
    private String mensagem;
}

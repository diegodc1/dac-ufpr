package com.aerolinha.sagas.deletarfuncionariosaga.eventos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoFuncUserDeletado {

    private String idUsuario;
    private String estadoUsuario;
    private String mensagem;

}

package com.aerolinha.sagas.eventos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoFuncInativado {

    private String idUsuario;
    private String nome;
    private String estadoFuncionario;
    private String mensagem;
    
}

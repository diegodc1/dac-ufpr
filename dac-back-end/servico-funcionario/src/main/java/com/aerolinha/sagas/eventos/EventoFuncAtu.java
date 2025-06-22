package com.aerolinha.sagas.eventos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoFuncAtu {
    private Long codigo;
    private String cpf;
    private String telefone;
    private String idUsuario;
    private String nome;
    private String email;
    private String mensagem;
}

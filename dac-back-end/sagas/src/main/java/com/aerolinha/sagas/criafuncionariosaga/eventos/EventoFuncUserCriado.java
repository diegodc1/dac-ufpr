package com.aerolinha.sagas.criafuncionariosaga.eventos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoFuncUserCriado {
    private String idUsuario; // vem do id criado no serviço de autenticação
    private String senhaUsuario;
    private String mensagem;
}

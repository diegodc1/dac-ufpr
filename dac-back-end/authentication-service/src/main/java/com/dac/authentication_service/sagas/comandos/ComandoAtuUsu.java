package com.dac.authentication_service.sagas.comandos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComandoAtuUsu {
    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String mensagem;
}

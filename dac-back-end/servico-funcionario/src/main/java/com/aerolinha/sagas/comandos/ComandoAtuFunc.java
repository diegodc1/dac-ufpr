package com.aerolinha.sagas.comandos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComandoAtuFunc {
    private Long idUsuario;
    private String nome;
    private String email;
    private String numeroTelefone;
    private String mensagem;
}

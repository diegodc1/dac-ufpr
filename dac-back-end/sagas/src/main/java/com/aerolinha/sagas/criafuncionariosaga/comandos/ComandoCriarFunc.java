package com.aerolinha.sagas.criafuncionariosaga.comandos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComandoCriarFunc {
    private String idUsuario;
    private String nome;
    private String cpf;
    private String email;
    private String numeroTelefone;
    private String mensagem;

}

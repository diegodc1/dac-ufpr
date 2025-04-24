package com.aerolinha.sagas.criafuncionariosaga.requisicoes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificarFuncionario {
    private String cpf;
    private String email;
    private String mensagem;
}

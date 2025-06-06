package com.aerolinha.sagas.resposta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificarFuncRes {

    private String mensagem;
    private Boolean comecaSaga;
    private String cpf;
}

package com.aerolinha.sagas.deletarfuncionariosaga.comandos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComandoDelFunc {
    private String idUsuario;
    private String mensagem;
}

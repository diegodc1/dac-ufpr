package com.aerolinha.dto.requisicao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovoFuncDTO {
    private String cpf;
    private String email;
    private String nome;
    private String telefone;
}

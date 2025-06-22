package com.aerolinha.dto.requisicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PutFuncDTO {
    private Long codigo;
    private String cpf;
    private String email;
    private String nome;
    private String telefone;
    private String senha;
}

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
    private String idUsuario;
    private String nome;
    private String email;
    private String numeroTelefone;
}

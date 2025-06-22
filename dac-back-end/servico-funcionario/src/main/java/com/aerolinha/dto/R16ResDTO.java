package com.aerolinha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R16ResDTO {
    private Long codigo;
    private String cpf;
    private String email;
    private String nome;
    private String telefone;
    private String tipo;
}

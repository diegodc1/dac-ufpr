package com.aerolinha.dto.resposta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class R18ResDTO {
    private Long codigo;
    private String cpf;
    private String email;
    private String nome;
    private String telefone;
}

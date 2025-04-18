package com.aerolinha.dto.resposta;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class ResGenDTO {

    private String mensagem;

    public ResGenDTO(String msg) {
        this.mensagem = msg;
    }
}

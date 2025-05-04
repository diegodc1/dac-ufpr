package com.aerolinha.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R16ResDTO {
    private UUID id;
    private String idUsuario;
    private String nome;
    private String cpf;
    private String email;
    private String numeroTelefone;
}

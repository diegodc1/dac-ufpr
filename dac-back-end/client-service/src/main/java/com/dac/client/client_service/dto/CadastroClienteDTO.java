package com.dac.client.client_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroClienteDTO {
    private String cpf;
    private String nome;
    private String email;
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;
}
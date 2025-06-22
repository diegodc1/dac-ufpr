package com.dac.client.client_service.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class ClienteRequestDTO {

    @NotNull
    @Size(min = 11, max = 11)
    private String cpf;

    @Email
    private String email;

    @NotBlank
    private String nome;

    @Pattern(regexp = "\\d{10,11}")
    private String telefone;

    @NotBlank
    private String senha;

}
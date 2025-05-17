package com.dac.client.client_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ClienteResponseDTO {

    @NotNull
    private Long codigo;

    @NotNull
    @Size(min = 11, max = 11)
    private String cpf;

    @Email
    private String email;

    private String nome;

    @PositiveOrZero
    private Integer saldoMilhas;
}

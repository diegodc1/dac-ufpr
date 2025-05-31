package com.dac.client.client_service.dto;

import com.dac.client.client_service.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroClienteDTO {
    private Long codigoCliente;
    private String cpf;
    private String email;
    private String nome;
    private Integer saldo_milhas;
    private EnderecoDTO endereco;

    public CadastroClienteDTO(Cliente cliente) {
        this.codigoCliente = cliente.getCodigoCliente();
        this.cpf = cliente.getCpf();
        this.email = cliente.getEmail();
        this.nome = cliente.getNome();
        this.saldo_milhas = cliente.getSaldoMilhas();
        this.endereco = new EnderecoDTO(cliente);
    }
}
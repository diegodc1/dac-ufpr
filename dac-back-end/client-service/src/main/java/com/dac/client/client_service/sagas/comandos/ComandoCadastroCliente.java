package com.dac.client.client_service.sagas.comandos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComandoCadastroCliente {
    private String mensagem = "ComandoCadastroCliente";
    private Long codigoCliente;
    private String cpf;
    private String nome;
    private String email;
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String cidade;
    private String bairro;
    private String uf;
    private String senha;
    private Integer saldo_milhas;
}
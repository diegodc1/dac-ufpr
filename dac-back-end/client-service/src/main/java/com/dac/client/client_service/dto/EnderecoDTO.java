package com.dac.client.client_service.dto;

import com.dac.client.client_service.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {
    private String cep;
    private String uf;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String complemento;

    public EnderecoDTO(Cliente cliente) {
        this.cep = cliente.getCep();
        this.rua = cliente.getRua();
        this.numero = cliente.getNumero();
        this.complemento = cliente.getComplemento();
        this.cidade = cliente.getCidade();
        this.uf = cliente.getUf();
        this.bairro = cliente.getBairro();
    }
}
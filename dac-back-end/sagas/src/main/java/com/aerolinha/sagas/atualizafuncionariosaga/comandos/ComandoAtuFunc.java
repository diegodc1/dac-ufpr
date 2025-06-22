package com.aerolinha.sagas.atualizafuncionariosaga.comandos;

import com.aerolinha.dto.requisicao.PutFuncDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComandoAtuFunc {
    private Long idUsuario;
    private String nome;
    private String email;
    private String numeroTelefone;
    private String mensagem;

    public ComandoAtuFunc(PutFuncDTO entidade) {
        this.idUsuario = entidade.getCodigo();
        this.nome = entidade.getNome();
        this.email = entidade.getEmail();
        this.numeroTelefone = entidade.getTelefone();
    }
}

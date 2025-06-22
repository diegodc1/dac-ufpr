package com.aerolinha.dto.resposta;

import com.aerolinha.sagas.criafuncionariosaga.eventos.EventoFuncCriado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class R17ResDTO {
    private Long codigo;
    private String cpf;
    private String email;
    private String nome;
    private String telefone;

    public R17ResDTO(EventoFuncCriado evento) {
        this.codigo = evento.getCodigo();
        this.cpf = evento.getCpf();
        this.email = evento.getEmail();
        this.nome = evento.getNome();
        this.telefone = evento.getTelefone();
    }

}

package com.aerolinha.dto.resposta;

import com.aerolinha.sagas.deletarfuncionariosaga.eventos.EventoFuncInativado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class R19ResDTO {
    private Long codigo;
    private String cpf;
    private String email;
    private String nome;
    private String telefone;

    public R19ResDTO(EventoFuncInativado evento) {
        this.codigo = evento.getCodigo();
        this.cpf = evento.getCpf();
        this.email = evento.getEmail();
        this.nome = evento.getNome();
        this.telefone = evento.getTelefone();
    }

}

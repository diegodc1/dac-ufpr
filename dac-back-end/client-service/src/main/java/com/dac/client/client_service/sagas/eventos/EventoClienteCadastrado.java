package com.dac.client.client_service.sagas.eventos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoClienteCadastrado {
    private String mensagem = "EventoClienteCadastrado";
    private String cpf;
    private String nome;
    private String email;
    private String login;
    private boolean sucesso;
    private String mensagemErro;
}
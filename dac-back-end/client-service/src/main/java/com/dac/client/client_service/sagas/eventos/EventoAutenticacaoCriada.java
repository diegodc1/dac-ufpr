package com.dac.client.client_service.sagas.eventos;
import com.dac.client.client_service.sagas.comandos.ComandoCadastroCliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoAutenticacaoCriada {
    private String mensagem = "EventoAutenticacaoCriada";
    private String login;
    private String senha;
    private String email;
    private boolean sucesso;
    private String mensagemErro;

    private ComandoCadastroCliente comando;
}

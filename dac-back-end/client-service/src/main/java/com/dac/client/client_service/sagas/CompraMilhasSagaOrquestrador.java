package com.dac.client.client_service.sagas;

import com.dac.client.client_service.sagas.eventos.EventoCompraMilhasIniciada;
import org.springframework.stereotype.Component;

@Component
public class CompraMilhasSagaOrquestrador {

    public void iniciarSaga(EventoCompraMilhasIniciada evento) {
        System.out.println("Iniciando saga de compra de milhas para o cliente: " + evento.getEmailCliente());
        System.out.println("Quantidade de milhas: " + evento.getQuantidadeMilhas());
        System.out.println("Valor pago: " + evento.getValorPago());

        try {
            if (evento.getQuantidadeMilhas() <= 0 || evento.getValorPago() <= 0) {
                throw new IllegalArgumentException("Dados inválidos para a compra de milhas.");
            }

            System.out.println("Reservando milhas...");
            boolean reservaConcluida = reservarMilhas(evento.getQuantidadeMilhas());
            
            if (reservaConcluida) {
                System.out.println("Milhas reservadas com sucesso.");
    
                confirmarCompra(evento);
            } else {
                System.out.println("Falha ao reservar milhas.");
                
                enviarEventoFalha(evento);
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar a saga: " + e.getMessage());
            enviarEventoFalha(evento);
        }
    }

    private boolean reservarMilhas(int quantidadeMilhas) {
     
        return quantidadeMilhas > 0; 
    }

    private void confirmarCompra(EventoCompraMilhasIniciada evento) {
        
        System.out.println("Compra confirmada para o cliente: " + evento.getEmailCliente());
    }

    private void enviarEventoFalha(EventoCompraMilhasIniciada evento) {
        
        System.out.println("Enviando evento de falha para o cliente: " + evento.getEmailCliente());
    }
}
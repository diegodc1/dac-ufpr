package com.dac.client.client_service.service;

import ch.qos.logback.core.net.server.Client;
import com.dac.client.client_service.model.Cliente;
import com.dac.client.client_service.model.TransacaoMilhas;
import com.dac.client.client_service.repository.ClienteRepository;
import com.dac.client.client_service.repository.TransacaoMilhasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransacaoMilhasService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TransacaoMilhasRepository transacaoMilhasRepository;

    private Cliente findClienteByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }


    public List<TransacaoMilhas> getExtratoMilhas(String email) {
        Cliente cliente = findClienteByEmail(email);
        return transacaoMilhasRepository.findByClienteOrderByDataHoraDesc(cliente);
    }


    @Transactional
    public TransacaoMilhas registrarEntradaMilhas(String email, int quantidadeMilhas, double valorPago, String descricao) {
        if (quantidadeMilhas <= 0) {
            throw new IllegalArgumentException("A quantidade de milhas deve ser positiva para entrada.");
        }

        Cliente cliente = findClienteByEmail(email);
        cliente.setSaldoMilhas(cliente.getSaldoMilhas() + quantidadeMilhas);
        clienteRepository.save(cliente);

        TransacaoMilhas transacao = new TransacaoMilhas();
        transacao.setCliente(cliente);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setQuantidade((long) quantidadeMilhas);
        transacao.setTipo("ENTRADA");
        transacao.setDescricao(descricao);
        transacao.setValorEmReais(BigDecimal.valueOf(valorPago));
        transacao.setCodigoReserva(null);

        return transacaoMilhasRepository.save(transacao);
    }

    @Transactional
    public TransacaoMilhas registrarSaidaMilhas(String email, int quantidadeMilhas, String codigoReserva, String descricao, double valorEmReais) {
        if (quantidadeMilhas <= 0) {
            throw new IllegalArgumentException("A quantidade de milhas deve ser positiva para saída.");
        }

        Cliente cliente = findClienteByEmail(email);
        if (cliente.getSaldoMilhas() < quantidadeMilhas) {
            throw new RuntimeException("Saldo de milhas insuficiente para esta transação.");
        }

        cliente.setSaldoMilhas(cliente.getSaldoMilhas() - quantidadeMilhas);
        clienteRepository.save(cliente);

        TransacaoMilhas transacao = new TransacaoMilhas();
        transacao.setCliente(cliente);
        transacao.setDataHora(LocalDateTime.now());
        transacao.setQuantidade((long) quantidadeMilhas);
        transacao.setTipo("SAÍDA");
        transacao.setDescricao(descricao);
        transacao.setCodigoReserva(codigoReserva);
        transacao.setValorEmReais(BigDecimal.valueOf(valorEmReais));

        return transacaoMilhasRepository.save(transacao);
    }
}

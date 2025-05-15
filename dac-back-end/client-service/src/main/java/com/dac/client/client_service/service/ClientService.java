package com.dac.backend.clientservice.service;

import com.dac.backend.clientservice.model.Cliente;
import com.dac.backend.clientservice.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deletar(String cpf) {
        clienteRepository.deleteById(cpf);
    }

    public Cliente buscarPorCodigo(Long codigo) {
        return clienteRepository.findByCodigo(codigo)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o código: " + codigo));
    }

    public List<Map<String, Object>> buscarTransacoesPorCliente(Long codigoCliente) {
        List<Map<String, Object>> transacoes = new ArrayList<>();

        Map<String, Object> transacao1 = new HashMap<>();
        transacao1.put("data", "2024-10-13T16:00:00Z-03:00");
        transacao1.put("valor_reais", 500.00);
        transacao1.put("quantidade_milhas", 100);
        transacao1.put("descricao", "COMPRA DE MILHAS");
        transacao1.put("codigo_reserva", "");
        transacao1.put("tipo", "ENTRADA");

        Map<String, Object> transacao2 = new HashMap<>();
        transacao2.put("data", "2024-10-13T20:00:00Z-03:00");
        transacao2.put("valor_reais", 250.00);
        transacao2.put("quantidade_milhas", 50);
        transacao2.put("descricao", "COMPRA DE MILHAS");
        transacao2.put("codigo_reserva", "");
        transacao2.put("tipo", "ENTRADA");

        transacoes.add(transacao1);
        transacoes.add(transacao2);

        return transacoes;
    }
}
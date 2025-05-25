package com.dac.client.client_service.service;

import com.dac.client.client_service.dto.CadastroClienteDTO;
import com.dac.client.client_service.exception.ClientAlreadyExistsException;
import com.dac.client.client_service.model.Cliente;
import com.dac.client.client_service.repository.ClienteRepository;
import com.dac.client.client_service.sagas.comandos.ComandoCadastroCliente;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente findByEmail(String email){
        return clienteRepository.findByEmail(email);
    };

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deletar(String cpf) {
        clienteRepository.deleteById(cpf);
    }

    public void iniciarCadastroCliente(CadastroClienteDTO cadastroDTO) throws JsonProcessingException {
        if (clienteRepository.existsByCpf(cadastroDTO.getCpf()) || clienteRepository.existsByEmail(cadastroDTO.getEmail()) ) {
            throw new ClientAlreadyExistsException();
        }

        // senha aleatoria
        Random random = new Random();
        String senha = String.format("%04d", random.nextInt(10000));

        // cria o comando para o serviço de autenticação
        ComandoCadastroCliente comando = new ComandoCadastroCliente();
        comando.setCpf(cadastroDTO.getCpf());
        comando.setNome(cadastroDTO.getNome());
        comando.setEmail(cadastroDTO.getEmail());
        comando.setCep(cadastroDTO.getEndereco().getCep());
        comando.setRua(cadastroDTO.getEndereco().getRua());
        comando.setNumero(cadastroDTO.getEndereco().getNumero());
        comando.setComplemento(cadastroDTO.getEndereco().getComplemento());
        comando.setCidade(cadastroDTO.getEndereco().getCidade());
        comando.setUf(cadastroDTO.getEndereco().getUf());
        comando.setSaldo_milhas(cadastroDTO.getSaldo_milhas());
        comando.setSenha(senha);

        // envia mensagem para o serviço de autenticação
        String mensagem = objectMapper.writeValueAsString(comando);
        rabbitTemplate.convertAndSend("CanalAut", mensagem);
    }

    public Cliente salvarCliente(ComandoCadastroCliente comando) {
        Cliente cliente = new Cliente();
        cliente.setCodigo(comando.getCodigo());
        cliente.setCpf(comando.getCpf());
        cliente.setNome(comando.getNome());
        cliente.setEmail(comando.getEmail());
        cliente.setCep(comando.getCep());
        cliente.setRua(comando.getRua());
        cliente.setComplemento(comando.getComplemento());
        cliente.setBairro(comando.getBairro());
        cliente.setCidade(comando.getCidade());
        cliente.setUf(comando.getUf());
        cliente.setNumero(comando.getNumero());
        cliente.setSaldoMilhas(comando.getSaldo_milhas() != null ? comando.getSaldo_milhas() : 0);

        return clienteRepository.save(cliente);
    }

    public Cliente atualizarPerfil(String email, CadastroClienteDTO perfilDTO) {
        Cliente cliente = findByEmail(email);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado");
        }
        
        cliente.setNome(perfilDTO.getNome());
        
        if (perfilDTO.getEndereco() != null) {
            cliente.setCep(perfilDTO.getEndereco().getCep());
            cliente.setRua(perfilDTO.getEndereco().getRua());
            cliente.setNumero(perfilDTO.getEndereco().getNumero());
            cliente.setComplemento(perfilDTO.getEndereco().getComplemento());
            cliente.setCidade(perfilDTO.getEndereco().getCidade());
            cliente.setUf(perfilDTO.getEndereco().getUf());
            if (perfilDTO.getEndereco().getBairro() != null) {
                cliente.setBairro(perfilDTO.getEndereco().getBairro());
            }
        }
        
        return clienteRepository.save(cliente);
    }

    public Cliente comprarMilhas(String email, int quantidade, double valorPago) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de milhas deve ser maior que zero");
        }
        
        if (valorPago <= 0) {
            throw new IllegalArgumentException("O valor pago deve ser maior que zero");
        }
        
        double valorMinimo = quantidade * 0.02;
        if (valorPago < valorMinimo) {
            throw new IllegalArgumentException("Valor insuficiente para a quantidade de milhas solicitada");
        }
        
        Cliente cliente = findByEmail(email);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado");
        }
        
        cliente.setSaldoMilhas(cliente.getSaldoMilhas() + quantidade);
        
        return clienteRepository.save(cliente);
    }
}
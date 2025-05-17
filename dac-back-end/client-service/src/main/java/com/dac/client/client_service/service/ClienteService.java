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
        if (clienteRepository.existsById(cadastroDTO.getCpf())) {
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
        cliente.setCidade(comando.getCidade());
        cliente.setUf(comando.getUf());
        cliente.setNumero(comando.getNumero());
        cliente.setSaldoMilhas(comando.getSaldo_milhas() != null ? comando.getSaldo_milhas() : 0);

        return clienteRepository.save(cliente);
    }
}
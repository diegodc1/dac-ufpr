package com.dac.client.client_service.controller;

import com.dac.client.client_service.components.EsperaResposta;
import com.dac.client.client_service.dto.CadastroClienteDTO;
import com.dac.client.client_service.dto.ClienteRequestDTO;
import com.dac.client.client_service.dto.ClienteResponseDTO;
import com.dac.client.client_service.exception.ClientAlreadyExistsException;
import com.dac.client.client_service.model.Cliente;
import com.dac.client.client_service.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EsperaResposta esperaResposta;

    @PostMapping
    public ResponseEntity<?> cadastrarCliente(@RequestBody CadastroClienteDTO cadastroDTO) {
        try {
            String cpf = cadastroDTO.getCpf();
            CompletableFuture<Cliente> future = new CompletableFuture<>();
            esperaResposta.aguardar(cpf, future);

            clienteService.iniciarCadastroCliente(cadastroDTO);

            Cliente cliente = future.get(10, TimeUnit.SECONDS);

            return ResponseEntity.status(HttpStatus.CREATED).body(new CadastroClienteDTO(cliente));

        }  catch (ClientAlreadyExistsException  e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (TimeoutException e) {
            return ResponseEntity.status(504).body("Tempo de espera excedido para criação do cliente.");
        }
        catch (Exception e) {
            return ResponseEntity.status(409).body("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    @GetMapping("/{login}")
    public CadastroClienteDTO getByLogin(@PathVariable String login) {
        return new CadastroClienteDTO(clienteService.findByEmail(login));
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listarTodos().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

//    @PostMapping
//    public ClienteResponseDTO salvar(@RequestBody ClienteRequestDTO clienteRequestDTO) {
//        Cliente cliente = toEntity(clienteRequestDTO);
//        Cliente clienteSalvo = clienteService.salvar(cliente);
//        return toResponseDTO(clienteSalvo);
//    }

    @DeleteMapping("/{cpf}")
    public void deletar(@PathVariable String cpf) {
        clienteService.deletar(cpf);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                Long.valueOf(cliente.getCpf()),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getNome(),
                cliente.getSaldoMilhas()
        );
    }

    private Cliente toEntity(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = new Cliente();
        cliente.setCpf(clienteRequestDTO.getCpf());
        cliente.setEmail(clienteRequestDTO.getEmail());
        cliente.setNome(clienteRequestDTO.getNome());
        return cliente;
    }
}
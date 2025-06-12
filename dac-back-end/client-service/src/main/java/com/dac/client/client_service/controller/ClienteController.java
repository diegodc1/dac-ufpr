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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@CrossOrigin
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

    @GetMapping("/{codigoCliente}")
    public ResponseEntity<?> getByCodigoCliente(@PathVariable Long codigoCliente) {
        Cliente cliente = clienteService.findByCodigo(codigoCliente);

        if (cliente != null) {
            return ResponseEntity.ok().body(new CadastroClienteDTO(cliente));
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/login/{login}")
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

    @GetMapping("/saldo-milhas")
    public ResponseEntity<Integer> getSaldoMilhas(@RequestHeader(value = "x-access-token", required = false) String token) {
        String email = getUserEmailFromToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Cliente cliente = clienteService.findByEmail(email);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(cliente.getSaldoMilhas());
    }

    @GetMapping("/perfil")
    public ResponseEntity<CadastroClienteDTO> getPerfilCliente(@RequestHeader(value = "x-access-token", required = false) String token) {
        String email = getUserEmailFromToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Cliente cliente = clienteService.findByEmail(email);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new CadastroClienteDTO(cliente));
    }

    @PutMapping("/perfil")
    public ResponseEntity<CadastroClienteDTO> atualizarPerfil(
            @RequestHeader(value = "x-access-token", required = false) String token,
            @RequestBody CadastroClienteDTO perfilDTO) {
        String email = getUserEmailFromToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Cliente clienteAtualizado = clienteService.atualizarPerfil(email, perfilDTO);
        return ResponseEntity.ok(new CadastroClienteDTO(clienteAtualizado));
    }

    @PostMapping("/comprar-milhas")
    public ResponseEntity<?> comprarMilhas(
            @RequestHeader(value = "x-access-token", required = false) String token,
            @RequestBody Map<String, Object> dados) {
        String email = getUserEmailFromToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Integer quantidade = (Integer) dados.get("quantidade");
        Double valorPago = Double.valueOf(dados.get("valorPago").toString());
        
        try {
            Cliente cliente = clienteService.comprarMilhas(email, quantidade, valorPago);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("saldoAtual", cliente.getSaldoMilhas());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private String getUserEmailFromToken(String token) {
        if (token == null) {
            return null;
        }
        
        try {
            // Esta é uma implementação temporária - deverá ser substituída pela validação real do token
            // Para fins de demonstração, estamos apenas retornando um email fixo
            return "user@example.com";
        } catch (Exception e) {
            return null;
        }
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
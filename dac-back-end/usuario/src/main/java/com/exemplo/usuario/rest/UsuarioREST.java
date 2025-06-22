package main.java.com.exemplo.usuario.rest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.reservas.controllers.PostMapping;
import com.example.reservas.controllers.RequestBody;

import br.net.web.project.project.model.LoginResponse;
import br.net.web.project.project.service.SendEmail;
import main.java.com.example.reservas.controllers.GetMapping;
import main.java.com.example.reservas.controllers.PathVariable;
import main.java.com.exemplo.usuario.database.UsuarioRepository;
import main.java.com.exemplo.usuario.model.Usuario;

@CrossOrigin
@RestController
public class UsuarioREST {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SendEmail testeEmail;

    @GetMapping("/usuarios")
    public List<Usuario> obterTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Usuario>> obterTodosClientes() {
        List<Usuario> usuarios = usuarioRepository.findByPerfil("CLIENTE");
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<List<Usuario>> obterClientePorId(@PathVariable("id") int id) {
        List<Usuario> usuarios = usuarioRepository.findByIdAndPerfil(id,"CLIENTE");
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<Usuario>> obterFUncionarioPorId(@PathVariable("id") int id) {
        List<Usuario> usuarios = usuarioRepository.findByIdAndPerfil(id,"FUNCIONARIO");
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/funcionarios")
    public ResponseEntity<List<Usuario>> obterTodosFuncionarios() {
        List<Usuario> usuarios = usuarioRepository.findByPerfil("FUNCIONARIO");
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/clientes/autocadastro")
    public ResponseEntity<String> inserirCliente(@RequestBody Usuario cliente) {
        if (usuarioRepository.findByEmail(cliente.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado");
        }

        cliente.setPerfil("CLIENTE");
        String senhaGerada = gerarSenhaAleatoria();
        cliente.setSenha(criptografarSenha(senhaGerada));

        usuarioRepository.save(cliente);
        testeEmail.sendEmail(cliente.getEmail(), "Sua nova senha", "Sua senha é: " + senhaGerada);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente cadastrado com sucesso.");
    }

    @PostMapping("/funcionario")
    public ResponseEntity<String> inserirFuncionario(@RequestBody Usuario funcionario) {
        if (usuarioRepository.findByEmail(funcionario.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado");
        }
//enviar senha
        funcionario.setPerfil("FUNCIONARIO");
        String senhaGerada = gerarSenhaAleatoria();
        funcionario.setSenha(criptografarSenha(senhaGerada));

        usuarioRepository.save(funcionario);
        testeEmail.sendEmail(funcionario.getEmail(), "Sua nova senha", "Sua senha é: " + senhaGerada);
        return ResponseEntity.status(HttpStatus.CREATED).body("Funcionário cadastrado com sucesso.");
    }


    @PutMapping("/funcionario/{id}")
    public ResponseEntity<String> atualizarCliente(
            @PathVariable("id") int id,
            @RequestBody Usuario funcionarioAtualizado) {
        
        return usuarioRepository.findById(id).map(funcionario -> {
            // Atualizar campos do cliente existente
            funcionario.setNome(funcionarioAtualizado.getNome());
            funcionario.setEmail(funcionarioAtualizado.getEmail());
            funcionario.setDataNascimento(funcionarioAtualizado.getDataNascimento());
            funcionario.setSenha(criptografarSenha(funcionarioAtualizado.getSenha()));
            usuarioRepository.save(funcionario);
            
            return ResponseEntity.ok("Cliente atualizado com sucesso.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado."));
    }
    
    @GetMapping("/funcionarios/redirecionar/{id}")
    public ResponseEntity<List<Usuario>> obterUsuariosParaRedirecionar(@PathVariable("id") int id) {
        List<Usuario> usuarios = usuarioRepository.findAll().stream()
                .filter(u -> u.getId() != id && "FUNCIONARIO".equals(u.getPerfil()))
                .collect(Collectors.toList());
        
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Usuario loginData) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginData.getEmail());

        if (usuario.isPresent() && verificarSenha(loginData.getSenha(), usuario.get().getSenha())) {
            return ResponseEntity.ok(new LoginResponse(usuario.get(), "Login bem-sucedido"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, "Credenciais inválidas"));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<String> removerUsuario(@PathVariable("id") int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            if ("FUNCIONARIO".equals(usuario.get().getPerfil()) &&
                    usuarioRepository.findByPerfil("FUNCIONARIO").size() == 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não é possível remover o único funcionário");
            }

            usuarioRepository.deleteById(id);
            return ResponseEntity.ok("Usuário removido com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }

    private boolean verificarSenha(String senhaDigitada, String senhaArmazenada) {
        return criptografarSenha(senhaDigitada).equals(senhaArmazenada);
    }

    private String criptografarSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar senha", e);
        }
    }

    private String gerarSenhaAleatoria() {
        return String.format("%04d", new Random().nextInt(10000));
    }

    
}

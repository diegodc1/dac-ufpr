package com.dac.authentication_service.securityService;

import com.dac.authentication_service.collection.User;
import com.dac.authentication_service.repository.UserRepository;
import com.dac.authentication_service.sagas.comandos.ComandoAtuUsu;
import com.dac.authentication_service.sagas.comandos.ComandoCadastroCliente;
import com.dac.authentication_service.sagas.comandos.ComandoCriarFuncUser;
import com.dac.authentication_service.sagas.comandos.ComandoDelFunc;
import com.dac.authentication_service.sagas.eventos.EventoAutenticacaoCriada;
import com.dac.authentication_service.sagas.eventos.EventoFuncUserCriado;
import com.dac.authentication_service.sagas.eventos.EventoFuncUserDeletado;
import com.dac.authentication_service.sagas.eventos.EventoUsuAtu;
import com.dac.authentication_service.security.TokenService;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public Boolean saveUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // R01 - autocadastro
    public EventoAutenticacaoCriada cadastrarCliente(ComandoCadastroCliente comando) {
        try {
            // verifica se já existe usuário com o login
            if (userRepository.findByLogin(comando.getEmail()).isPresent()) {
                return new EventoAutenticacaoCriada(
                        "EventoAutenticacaoCriada",
                        comando.getCpf(),
                        null,
                        comando.getEmail(),
                        false,
                        "Usuário já existe",
                        comando
                );
            }

            // cria o novo usuário no mongodb
            User usuario = new User();
            usuario.setLogin(comando.getEmail());
            usuario.setPassword(passwordEncoder.encode(comando.getSenha()));
            usuario.setRole("CLIENTE");
            userRepository.save(usuario);

            return new EventoAutenticacaoCriada(
                    "EventoAutenticacaoCriada",
                    comando.getCpf(),
                    comando.getSenha(),
                    comando.getEmail(),
                    true,
                    null,
                    comando
            );
        } catch (Exception e) {
            return new EventoAutenticacaoCriada(
                    "EventoAutenticacaoCriada",
                    comando.getCpf(),
                    null,
                    comando.getEmail(),
                    false,
                    e.getMessage(),
                    comando
            );
        }
    }


    // R17
    @Transactional
    public EventoFuncUserCriado novoFuncionario(ComandoCriarFuncUser comando) {

        String senha = gerarSenhaAleatoria();

        User user = User.builder()
                .name(comando.getNome())
                .login(comando.getEmail())
                .password(passwordEncoder.encode(senha))
                .role("FUNCIONARIO")
                .userStatus("ATIVO")
                .build();

        user = userRepository.save(user);

        EventoFuncUserCriado evento = EventoFuncUserCriado.builder()
                .idUsuario(user.getId())
                .senhaUsuario(senha)
                .mensagem("EventoFuncUserCriado")
                .build();

        return evento;
    }

    private String gerarSenhaAleatoria() {

        String CARACTERES = "0123456789";

        int TAMANHO_SENHA = 4;

        SecureRandom aleatorio = new SecureRandom();

        StringBuilder construtorString = new StringBuilder(TAMANHO_SENHA);

        for (int i = 0; i < TAMANHO_SENHA; i++) {
            int indice = aleatorio.nextInt(CARACTERES.length());
            construtorString.append(CARACTERES.charAt(indice));
        }

        return construtorString.toString();
    }

    public String generateToken(String username) {
        return tokenService.generateToken(username);
    }

    public void validateToken(String token) {
        tokenService.validateToken(token);
    }

    // R18
    @Transactional
    public EventoUsuAtu atualizarFuncionario(ComandoAtuUsu comando) {

        User user = userRepository.findById(comando.getIdUsuario()).orElse(null);

        user.setName(comando.getNome());
        user.setLogin(comando.getEmail());
        user.setPassword(passwordEncoder.encode(comando.getSenha()));

        user = userRepository.save(user);

        EventoUsuAtu evento = EventoUsuAtu.builder()
                .nome(user.getName())
                .email(user.getLogin())
                .mensagem("EventoUsuAtu")
                .build();

        return evento;
    }

    // R19
    @Transactional
    public EventoFuncUserDeletado removerFuncionario(ComandoDelFunc comando) {

        User user = userRepository.findById(comando.getIdUsuario()).orElse(null);

        user.setUserStatus("INATIVO");

        user = userRepository.save(user);

        EventoFuncUserDeletado evento = EventoFuncUserDeletado.builder()

                .idUsuario(user.getId())
                .estadoUsuario(user.getUserStatus())
                .mensagem("EventoFuncUserDeletado")
                .build();

        return evento;

    }

}

package com.aerolinha.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerolinha.modelo.Funcionario;
import com.aerolinha.repositorio.FuncionarioRepositorio;
import com.aerolinha.sagas.comandos.ComandoCriarFunc;
import com.aerolinha.sagas.comandos.ComandoInativarFunc;
import com.aerolinha.sagas.consultas.VerificarFuncionario;
import com.aerolinha.sagas.eventos.EventoFuncCriado;
import com.aerolinha.sagas.eventos.EventoFuncInativado;
import com.aerolinha.sagas.resposta.VerificarFuncRes;

import jakarta.transaction.Transactional;

@Service
public class ServicoSagas {

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    // R17
    public VerificarFuncRes verificarFuncionario(VerificarFuncionario consulta) {

        Funcionario funcionario = funcionarioRepositorio.findByCpfAndEmail(consulta.getCpf(), consulta.getEmail());

        if (funcionario != null) {

            return VerificarFuncRes.builder()
                    .mensagem("Funcionário já existe!")
                    .comecaSaga(false)
                    .cpf(consulta.getCpf())
                    .build();

        }

        funcionario = funcionarioRepositorio.getFuncionarioByCpf(consulta.getCpf());

        if (funcionario != null) {

            return VerificarFuncRes.builder()
                    .mensagem("Este CPF já existe no sistema!")
                    .comecaSaga(false)
                    .cpf(consulta.getCpf())
                    .build();

        }

        funcionario = funcionarioRepositorio.getFuncionarioByEmail(consulta.getEmail());

        if (funcionario != null) {

            return VerificarFuncRes.builder()
                    .mensagem("Este email já existe no sistema!")
                    .comecaSaga(false)
                    .cpf(consulta.getCpf())
                    .build();

        }

        return VerificarFuncRes.builder()
                .mensagem("Funcionário adicionado ao sistema!")
                .comecaSaga(true)
                .cpf(consulta.getCpf())
                .build();

    }

    // R17 - cria registro funcionário
    public EventoFuncCriado criarFuncionario(ComandoCriarFunc comando) {

        Funcionario funcionario = Funcionario.builder()
                .idUsuario(comando.getIdUsuario())
                .nome(comando.getNome())
                .cpf(comando.getCpf())
                .email(comando.getEmail())
                .numeroTelefone(comando.getNumeroTelefone())
                .estado("ATIVO")
                .build();

        funcionario = funcionarioRepositorio.save(funcionario);

        EventoFuncCriado evento = EventoFuncCriado.builder()
                .codigo(funcionario.getIdFuncionario())
                .cpf(funcionario.getCpf())
                .email(funcionario.getEmail())
                .nome(funcionario.getNome())
                .telefone(funcionario.getNumeroTelefone())
                .mensagem("EventoFuncCriado")
                .build();

        return evento;
    }

    // R19
    @Transactional
    public EventoFuncInativado inativarFuncionario(ComandoInativarFunc comando) {

        Funcionario funcionario = funcionarioRepositorio.findByIdUsuario(comando.getUsuarioId());

        funcionario.setEstado(comando.getEstadoUsuario());

        funcionario = funcionarioRepositorio.save(funcionario);

        EventoFuncInativado evento = EventoFuncInativado.builder()
                .idUsuario(funcionario.getIdUsuario())
                .nome(funcionario.getNome())
                .estadoFuncionario(funcionario.getEstado())
                .mensagem("EventoFuncInativado")
                .build();

        return evento;

    }

}

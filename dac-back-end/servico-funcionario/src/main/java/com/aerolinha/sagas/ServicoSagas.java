package com.aerolinha.sagas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerolinha.modelo.Funcionario;
import com.aerolinha.repositorio.FuncionarioRepositorio;
import com.aerolinha.sagas.consultas.VerificarFuncionario;
import com.aerolinha.sagas.resposta.VerificarFuncRes;

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
                    .build();

        }

        funcionario = funcionarioRepositorio.getFuncionarioByCpf(consulta.getCpf());

        if (funcionario != null) {

            return VerificarFuncRes.builder()
                    .mensagem("Este CPF já existe no sistema!")
                    .comecaSaga(false)
                    .build();

        }

        funcionario = funcionarioRepositorio.getFuncionarioByEmail(consulta.getEmail());

        if (funcionario != null) {

            return VerificarFuncRes.builder()
                    .mensagem("Este email já existe no sistema!")
                    .comecaSaga(false)
                    .build();

        }

        return VerificarFuncRes.builder()
                .mensagem("Começar saga")
                .comecaSaga(true)
                .build();

    }

}

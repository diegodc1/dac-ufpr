package com.aerolinha.servico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerolinha.dto.R16ResDTO;
import com.aerolinha.modelo.Funcionario;
import com.aerolinha.repositorio.FuncionarioRepositorio;

@Service
public class SerFuncImpl implements ServicoFuncionario {

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    // R16
    @Override
    public List<R16ResDTO> getFuncionariosAtivos() {

        List<R16ResDTO> dto = new ArrayList<>();

        List<Funcionario> listaFuncionarios = funcionarioRepositorio.getFuncionariosAtivos();

        for (Funcionario funcionario : listaFuncionarios) {

            R16ResDTO empDto = R16ResDTO.builder()
                    .id(funcionario.getIdFuncionario())
                    .idUsuario(funcionario.getIdUsuario())
                    .nome(funcionario.getNome())
                    .cpf(funcionario.getCpf())
                    .email(funcionario.getEmail())
                    .numeroTelefone(this.formatarTelefone(funcionario.getNumeroTelefone()))
                    .build();

            dto.add(empDto);
        }

        return dto;

    }

    public String formatarTelefone(String numeroTelefone) {

        // 41991932059
        return String.format("(%s) %s-%s",
                numeroTelefone.substring(0, 2),
                numeroTelefone.substring(2, 7),
                numeroTelefone.substring(7));
    }

}

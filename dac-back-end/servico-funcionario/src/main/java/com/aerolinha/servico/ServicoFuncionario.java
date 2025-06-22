package com.aerolinha.servico;

import java.util.List;

import com.aerolinha.dto.R16ResDTO;

public interface ServicoFuncionario {
    // R16
    List<R16ResDTO> getFuncionariosAtivos();

    R16ResDTO findByEmail(String email);
}

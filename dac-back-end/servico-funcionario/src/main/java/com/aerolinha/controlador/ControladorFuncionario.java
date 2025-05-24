package com.aerolinha.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerolinha.dto.R16ResDTO;
import com.aerolinha.servico.ServicoFuncionario;

@RestController
@CrossOrigin
@RequestMapping("funcionario")
public class ControladorFuncionario {

    @Autowired
    private ServicoFuncionario servicoFuncionario;

    // R16
    @GetMapping("/funcionarios")
    public ResponseEntity<List<R16ResDTO>> getFuncionariosAtivos() {
        List<R16ResDTO> dto = servicoFuncionario.getFuncionariosAtivos();
        return ResponseEntity.ok().body(dto);
    }

}

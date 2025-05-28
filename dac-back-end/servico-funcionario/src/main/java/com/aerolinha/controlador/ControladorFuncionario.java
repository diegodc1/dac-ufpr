package com.aerolinha.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/funcionarios/{login}")
    public R16ResDTO getByLogin(@PathVariable String login) {
        return servicoFuncionario.findByEmail(login);
    }
}

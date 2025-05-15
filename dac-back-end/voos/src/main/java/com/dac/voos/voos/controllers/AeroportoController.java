package com.dac.voos.voos.controllers;

import com.dac.voos.voos.dto.AeroportoDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.services.AeroportoService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/aeroportos")
public class AeroportoController {

    @Autowired
    private AeroportoService aeroportoService;

    @PostMapping("/newAeroporto")
    public ResponseEntity<String> newAeroporto(@RequestBody AeroportoDTO aeroportoDTO){
        aeroportoService.saveAeroporto(
                aeroportoDTO.codigo(),
                aeroportoDTO.nome(),
                aeroportoDTO.cidade(),
                aeroportoDTO.uf()
        );
        return ResponseEntity.ok("Criado aeroporto");
    }
    @GetMapping
    public  ResponseEntity<List<Aeroporto>> todosAeroportos(){
        List<Aeroporto> aeroportos = aeroportoService.listAeroporto();
        return ResponseEntity.ok(aeroportos);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> burcarPorCodigo(@PathVariable String codigo){
        Optional<Aeroporto> aeroporto = aeroportoService.listAeroportoId(codigo);
        return aeroporto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> removerAeroporto( @PathVariable String codigo){
        try {
            aeroportoService.removerAeroporto(codigo);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Erro ao remover");
        }
    }
}

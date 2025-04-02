package com.dac.voos.voos.controllers;

import com.dac.voos.voos.dto.AeroportoDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.services.AeroportoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/aeroporto")
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
    @GetMapping("/todosAeroportos")
    public  ResponseEntity<List<Aeroporto>> todosAeroportos(){
        List<Aeroporto> aeroportos = aeroportoService.listAeroporto();
        return ResponseEntity.ok(aeroportos);
    }


}

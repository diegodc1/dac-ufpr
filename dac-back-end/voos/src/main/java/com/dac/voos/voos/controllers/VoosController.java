package com.dac.voos.voos.controllers;

import com.dac.voos.voos.dto.VooDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.entitys.Voos;
import com.dac.voos.voos.repositorys.AeroportoRepository;
import com.dac.voos.voos.repositorys.EstadoVooRepository;
import com.dac.voos.voos.repositorys.VooRepository;
import com.dac.voos.voos.services.VooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/voos")
public class VoosController {
    @Autowired
   private VooService vooService;
    @Autowired
    private EstadoVooRepository estadoVooRepository;
    @Autowired
    private AeroportoRepository aeroportoRepository;

    @PostMapping("/newVoo")
   public ResponseEntity<String> newVoos(@RequestBody VooDTO vooDTO){
       vooService.saveVoos(vooDTO);
       return ResponseEntity.ok("Criado voo com sucesso");
   }
   @GetMapping("/todosVoos")
   public ResponseEntity<List<Voos>> todosVoos(){
        List<Voos> voos = vooService.listVoos();
        return ResponseEntity.ok(voos);
   }
}

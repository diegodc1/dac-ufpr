package com.dac.voos.voos.services;

import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.repositorys.EstadoVooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoVooService {

    @Autowired
    private EstadoVooRepository estadoVooRepository;

    public EstadoVoo saveEstadoVoo(String sigla, String descricao){
            EstadoVoo newEstadoVoo = new EstadoVoo();
            newEstadoVoo.setDescricao(descricao);
            newEstadoVoo.setSigla(sigla);
           return estadoVooRepository.save(newEstadoVoo);
    }

    public List<EstadoVoo> listEstadoVoo(){
        return estadoVooRepository.findAll();
    }
    public Optional<EstadoVoo> listEstadoVooId(Long id){
        return estadoVooRepository.findById(id);
    }

    public void removerEstadoVoo(Long id){
        Optional<EstadoVoo> estadoVoo = estadoVooRepository.findById(id);
        if (estadoVoo.isPresent()){
            estadoVooRepository.deleteById(id);
        }else {
            throw new RuntimeException("Id nao encontrado");

        }
    }
}

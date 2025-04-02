package com.dac.voos.voos.services;

import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.repositorys.AeroportoRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AeroportoService {
    @Autowired
    private AeroportoRepository aeroportoRepository;

    public Aeroporto saveAeroporto(String codigo, String nome, String uf, String cidade){
        Aeroporto newAeroporto = new Aeroporto();
        newAeroporto.setUf(uf);
        newAeroporto.setNome(nome);
        newAeroporto.setCidade(cidade);
        newAeroporto.setCodigo(codigo);
        return aeroportoRepository.save(newAeroporto);
    }

    public List<Aeroporto> listAeroporto(){
        return aeroportoRepository.findAll();
    }

    public void removerAeroporto(String codigo){
        Optional<Aeroporto> aeroporto = aeroportoRepository.findById(codigo);
        if(aeroporto.isPresent()){
            aeroportoRepository.deleteById(codigo);
        }else {
            throw new RuntimeException("Codigo nao encontrado");
        }
    }
}

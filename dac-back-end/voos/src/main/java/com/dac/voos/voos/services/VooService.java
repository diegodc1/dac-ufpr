package com.dac.voos.voos.services;

import com.dac.voos.voos.dto.VooDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.entitys.Voos;
import com.dac.voos.voos.repositorys.AeroportoRepository;
import com.dac.voos.voos.repositorys.EstadoVooRepository;
import com.dac.voos.voos.repositorys.VooRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VooService {

    @Autowired
    private VooRepository vooRepository;
    @Autowired
    private EstadoVooRepository estadoVooRepository;
    @Autowired
    private AeroportoRepository aeroportoRepository;

    public Voos saveVoos(VooDTO vooDTO){

        Aeroporto origem = aeroportoRepository.findById(vooDTO.aeroportoOrigem().getCodigo())
                .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado"));

        Aeroporto destino = aeroportoRepository.findById(vooDTO.aeroportoDestino().getCodigo())
                .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado"));

        EstadoVoo estado = estadoVooRepository.findById(vooDTO.estadoVoo().getId())
                .orElseThrow(() -> new RuntimeException("Estado do voo não encontrado"));

        Voos newVoos = new Voos();

        newVoos.setEstadoVoo(vooDTO.estadoVoo());
        newVoos.setAeroportoDestino(destino);
        newVoos.setAeroportoOrigem(origem);
        newVoos.setData_hora(vooDTO.data_hora());
        newVoos.setQuantidadePoltronasOculpadas(vooDTO.quantidadePoltronasOculpadas());
        newVoos.setQuantidadePoltronasTotal(vooDTO.quantidadePoltronasTotal());
        newVoos.setValorPassagem(vooDTO.valorPassagem());
        return vooRepository.save(newVoos);
    }
    public List<Voos> listVoos(){
        return  vooRepository.findAll();
    }

    public void removerVoos(Long codigo){
        Optional <Voos> voos = vooRepository.findById(codigo);
        if (voos.isPresent()){
            vooRepository.deleteById(codigo);
        }else {
            throw new RuntimeException("Erro ao encontra o voo");
        }
    }
}

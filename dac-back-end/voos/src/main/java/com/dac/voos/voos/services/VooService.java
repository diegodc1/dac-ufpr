package com.dac.voos.voos.services;

import com.dac.voos.voos.dto.VooDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.entitys.Voos;
import com.dac.voos.voos.repositorys.AeroportoRepository;
import com.dac.voos.voos.repositorys.EstadoVooRepository;
import com.dac.voos.voos.repositorys.VooRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Optional<Voos> listVoosCod(Long codigo){
        return vooRepository.findById(codigo);
    }

    public void removerVoos(Long codigo){
        Optional <Voos> voos = vooRepository.findById(codigo);
        if (voos.isPresent()){
            vooRepository.deleteById(codigo);
        }else {
            throw new RuntimeException("Erro ao encontra o voo");
        }
    }

    public List<Voos> buscarVoosPorDataOrigemDestino(LocalDate data, Aeroporto origem, Aeroporto destino) {
        LocalDateTime dataInicio = data.atStartOfDay();
        LocalDateTime dataFim = data.atTime(LocalTime.MAX);
        return vooRepository.buscarPorDataHoraOrigemDestino(dataInicio, dataFim, origem, destino);
    }
    public List<Voos> buscarVoosPorIntervaloDeDatas(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return vooRepository.buscarVoosPorIntervaloDeDatas(dataInicio, dataFim);
    }



    private Map<String, Object> converterVooParaJson(Voos voo) {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("codigo", voo.getCodigo());
        jsonResponse.put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString().replace("Z", "-03:00"));
        jsonResponse.put("valor_passagem", voo.getValorPassagem());
        jsonResponse.put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
        jsonResponse.put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
        jsonResponse.put("estado", voo.getEstadoVoo().getSigla()); // Ou getDescricao()
        jsonResponse.put("codigo_aeroporto_origem", voo.getAeroportoOrigem().getCodigo());
        jsonResponse.put("codigo_aeroporto_destino", voo.getAeroportoDestino().getCodigo());
        return jsonResponse;
    }
}

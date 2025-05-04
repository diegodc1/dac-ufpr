package com.dac.voos.voos.services;

import com.dac.voos.voos.dto.VooDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.entitys.Voos;
import com.dac.voos.voos.repositorys.AeroportoRepository;
import com.dac.voos.voos.repositorys.EstadoVooRepository;
import com.dac.voos.voos.repositorys.VooRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public Voos saveNewVoo(VooDTO vooDTO) {
        Aeroporto origem = aeroportoRepository.findByCodigo(vooDTO.codigo_aeroporto_origem())
                .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado"));

        Aeroporto destino = aeroportoRepository.findByCodigo(vooDTO.codigo_aeroporto_destino())
                .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado"));

        EstadoVoo estadoConfirmado = estadoVooRepository.findBySigla("CONFIRMADO")
                .orElseThrow(() -> new RuntimeException("Estado 'CONFIRMADO' não encontrado"));

        Voos newVoo = new Voos();
        newVoo.setEstadoVoo(estadoConfirmado);
        newVoo.setAeroportoDestino(destino);
        newVoo.setAeroportoOrigem(origem);
        newVoo.setData_hora(vooDTO.data().toLocalDateTime());
        newVoo.setQuantidadePoltronasOculpadas(vooDTO.quantidade_poltronas_ocupadas());
        newVoo.setQuantidadePoltronasTotal(vooDTO.quantidade_poltronas_total());
        newVoo.setValorPassagem(BigDecimal.valueOf(vooDTO.valor_passagem()));
        return vooRepository.save(newVoo);
    }


    public Map<String, Object> converterVooParaJsonComEstadoConfirmado(Voos voo) {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("codigo", voo.getCodigo());
        jsonResponse.put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString().replace("Z", "-03:00"));
        jsonResponse.put("valor_passagem", voo.getValorPassagem());
        jsonResponse.put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
        jsonResponse.put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
        jsonResponse.put("estado", voo.getEstadoVoo().getSigla());
        jsonResponse.put("codigo_aeroporto_origem", voo.getAeroportoOrigem().getCodigo());
        jsonResponse.put("codigo_aeroporto_destino", voo.getAeroportoDestino().getCodigo());
        return jsonResponse;
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
        jsonResponse.put("estado", voo.getEstadoVoo().getSigla());
        jsonResponse.put("codigo_aeroporto_origem", voo.getAeroportoOrigem().getCodigo());
        jsonResponse.put("codigo_aeroporto_destino", voo.getAeroportoDestino().getCodigo());
        return jsonResponse;
    }

    private static final Logger logger = LoggerFactory.getLogger(VooService.class);


    @Transactional
    public ResponseEntity<?> cancelarVoo(Long codigoVoo) {
        logger.info("Tentando cancelar o voo com código: {}", codigoVoo);
        Optional<Voos> vooOptional = vooRepository.findById(codigoVoo);

        if (vooOptional.isEmpty()) {
            logger.warn("Voo com código {} não encontrado.", codigoVoo);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Voos voo = vooOptional.get();
        logger.info("Voo encontrado: {}", voo.getCodigo());
        Optional<EstadoVoo> estadoCanceladoOptional = estadoVooRepository.findBySigla("CANCELADO");

        if (estadoCanceladoOptional.isEmpty()) {
            logger.error("Estado 'CANCELADO' não encontrado no banco de dados.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EstadoVoo estadoCancelado = estadoCanceladoOptional.get();
        logger.info("Estado 'CANCELADO' encontrado: {}", estadoCancelado.getSigla());
        voo.setEstadoVoo(estadoCancelado);
        vooRepository.save(voo);
        logger.info("Voo {} cancelado com sucesso.", voo.getCodigo());

        return new ResponseEntity<>(converterVooParaJson(voo), HttpStatus.OK);
    }

}

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
import java.time.*;
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
        newVoo.setData_hora(vooDTO.data().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        newVoo.setQuantidadePoltronasOculpadas(vooDTO.quantidade_poltronas_ocupadas());
        newVoo.setQuantidadePoltronasTotal(vooDTO.quantidade_poltronas_total());
        newVoo.setValorPassagem(BigDecimal.valueOf(vooDTO.valor_passagem()));
        return vooRepository.save(newVoo);
    }


    public Map<String, Object> converterVooParaJsonCriado(Voos voo) {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("codigo", voo.getCodigo());
        jsonResponse.put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString());
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

    @Transactional
    public void removerVoos(Long codigo){
        Optional <Voos> voos = vooRepository.findById(codigo);
        if (voos.isPresent()){
            vooRepository.deleteById(codigo);
        }else {
            throw new RuntimeException("Voo com codigo:" + codigo+ "Não encontrado");
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


//
//    private Map<String, Object> converterVooParaJson(Voos voo) {
//        Map<String, Object> jsonResponse = new HashMap<>();
//        jsonResponse.put("codigo", voo.getCodigo());
//        jsonResponse.put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString().replace("Z", "-03:00"));
//        jsonResponse.put("valor_passagem", voo.getValorPassagem());
//        jsonResponse.put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
//        jsonResponse.put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
//        jsonResponse.put("estado", voo.getEstadoVoo().getSigla());
//        jsonResponse.put("codigo_aeroporto_origem", voo.getAeroportoOrigem().getCodigo());
//        jsonResponse.put("codigo_aeroporto_destino", voo.getAeroportoDestino().getCodigo());
//        return jsonResponse;
//    }

    private static final Logger logger = LoggerFactory.getLogger(VooService.class);


    @Transactional
    public ResponseEntity<?> cancelarVoo(Long codigoVoo) {
        logger.info("Tentando cancelar o voo com código: {}", codigoVoo);
        Optional<Voos> vooOptional = vooRepository.findById(codigoVoo);

        if (vooOptional.isEmpty()) {
            logger.warn("Voo com código {} não encontrado.", codigoVoo);
            Map<String, String> erroResponce = new HashMap<>();
            erroResponce.put("mensage", "Voo não encontrado");
            return new ResponseEntity<>(erroResponce,HttpStatus.NOT_FOUND);
        }

        Voos voo = vooOptional.get();
        logger.info("Voo encontrado: {}", voo.getCodigo());
        Optional<EstadoVoo> estadoCanceladoOptional = estadoVooRepository.findBySigla("CANCELADO");

        if (estadoCanceladoOptional.isEmpty()) {
            logger.error("Estado 'CANCELADO' não encontrado no banco de dados.");
            Map<String, String> errorResponce = new HashMap<>();
            errorResponce.put("mensage", "Erro interno: Estado 'CANCELADO' não encontrado");
            return new ResponseEntity<>(errorResponce,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EstadoVoo estadoCancelado = estadoCanceladoOptional.get();
        logger.info("Estado 'CANCELADO' encontrado: {}", estadoCancelado.getSigla());
        voo.setEstadoVoo(estadoCancelado);
        Voos vooAtualizado = vooRepository.save(voo);
        logger.info("Voo {} cancelado com sucesso.", voo.getCodigo());

        Map<String, Object> retornoVooCancelado = new HashMap<>();
        retornoVooCancelado.put("Codigo", vooAtualizado.getCodigo());
        retornoVooCancelado.put("data", vooAtualizado.getData_hora().atOffset(ZoneOffset.of("-03:00")).toString());
        retornoVooCancelado.put("valor_passagem", vooAtualizado.getValorPassagem());
        retornoVooCancelado.put("quantidade_poltronas_total", vooAtualizado.getQuantidadePoltronasTotal());
        retornoVooCancelado.put("quantidade_poltronas_ocupadas", vooAtualizado.getQuantidadePoltronasOculpadas());
        retornoVooCancelado.put("estado", vooAtualizado.getEstadoVoo().getSigla());
        retornoVooCancelado.put("aeroporto_origem", new HashMap<String, String>() {{
            put("codigo", vooAtualizado.getAeroportoOrigem().getCodigo());
            put("nome", vooAtualizado.getAeroportoOrigem().getNome());
            put("cidade", vooAtualizado.getAeroportoOrigem().getCidade());
            put("uf", vooAtualizado.getAeroportoOrigem().getUf());
        }});
        retornoVooCancelado.put("aeroporto_destino", new HashMap<String, String>() {{
            put("codigo", vooAtualizado.getAeroportoDestino().getCodigo());
            put("nome", vooAtualizado.getAeroportoDestino().getNome());
            put("cidade", vooAtualizado.getAeroportoDestino().getCidade());
            put("uf", vooAtualizado.getAeroportoDestino().getUf());
        }});


        return new ResponseEntity<>(retornoVooCancelado, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> atualizarEstadoVoo(Long codigoVoo, String novoEstadoSigla) {
        logger.info("Tentando atualizar o estado do voo com código {} para o estado: {}", codigoVoo, novoEstadoSigla);
        Optional<Voos> vooOptional = vooRepository.findById(codigoVoo);

        if (vooOptional.isEmpty()) {
            logger.warn("Voo com código {} não encontrado para atualização de estado.", codigoVoo);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", "Voo não encontrado.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Voos voo = vooOptional.get();
        logger.info("Voo encontrado: {}", voo.getCodigo());

        Optional<EstadoVoo> estadoDesejadoOptional = estadoVooRepository.findBySigla(novoEstadoSigla);

        if (estadoDesejadoOptional.isEmpty()) {
            logger.error("Estado '{}' não encontrado no banco de dados.", novoEstadoSigla);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", "Erro interno: Estado '" + novoEstadoSigla + "' não configurado.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EstadoVoo novoEstado = estadoDesejadoOptional.get();
        logger.info("Estado '{}' encontrado: {}", novoEstadoSigla, novoEstado.getSigla());

        voo.setEstadoVoo(novoEstado);
        Voos vooAtualizado = vooRepository.save(voo);
        logger.info("Voo {} atualizado para o estado {} com sucesso.", voo.getCodigo(), novoEstadoSigla);

        Map<String, Object> retornoVooAtualizado = converterVooParaJsonRespostaComAeroportosCompletos(vooAtualizado);

        return new ResponseEntity<>(retornoVooAtualizado, HttpStatus.OK);
    }

    private Map<String, Object> converterVooParaJsonRespostaComAeroportosCompletos(Voos voo) {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("codigo", voo.getCodigo());
        jsonResponse.put("data", voo.getData_hora().atOffset(ZoneOffset.of("-03:00")).toString());
        jsonResponse.put("valor_passagem", voo.getValorPassagem());
        jsonResponse.put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
        jsonResponse.put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
        jsonResponse.put("estado", voo.getEstadoVoo().getSigla());
        jsonResponse.put("aeroporto_origem", new HashMap<String, String>() {{
            put("codigo", voo.getAeroportoOrigem().getCodigo());
            put("nome", voo.getAeroportoOrigem().getNome());
            put("cidade", voo.getAeroportoOrigem().getCidade());
            put("uf", voo.getAeroportoOrigem().getUf());
        }});
        jsonResponse.put("aeroporto_destino", new HashMap<String, String>() {{
            put("codigo", voo.getAeroportoDestino().getCodigo());
            put("nome", voo.getAeroportoDestino().getNome());
            put("cidade", voo.getAeroportoDestino().getCidade());
            put("uf", voo.getAeroportoDestino().getUf());
        }});
        return jsonResponse;
    }
}

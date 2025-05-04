package com.dac.voos.voos.controllers;

import com.dac.voos.voos.dto.VooDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.entitys.Voos;
import com.dac.voos.voos.repositorys.AeroportoRepository;
import com.dac.voos.voos.repositorys.EstadoVooRepository;
import com.dac.voos.voos.repositorys.VooRepository;
import com.dac.voos.voos.services.VooService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private static final Logger logger = LoggerFactory.getLogger(VoosController.class);


    @PostMapping
    public ResponseEntity<Map<String, Object>> newVoo(@RequestBody VooDTO vooDTO) {
        logger.info("VooDTO recebido no controller: {}", vooDTO);
        Voos novoVoo = vooService.saveNewVoo(vooDTO);
        Map<String, Object> retorno = vooService.converterVooParaJsonComEstadoConfirmado(novoVoo);
        return new ResponseEntity<>(retorno, HttpStatus.CREATED);
    }


   @GetMapping("/todosVoos")
   public ResponseEntity<List<Voos>> todosVoos(){
        List<Voos> voos = vooService.listVoos();
        return ResponseEntity.ok(voos);
   }
    @GetMapping("/voos/{codigo}")
    public ResponseEntity<?> buscarCodigo(@PathVariable Long codigo) {
        Optional<Voos> voosOptional = vooService.listVoosCod(codigo);

        if (voosOptional.isPresent()) {
            Voos voo = voosOptional.get();
            Map<String, Object> retorno = new HashMap<>();
            retorno.put("codigo", voo.getCodigo());
            retorno.put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString().replace("Z", "-03:00"));
            retorno.put("valor_passagem", voo.getValorPassagem());
            retorno.put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
            retorno.put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
            retorno.put("estado", voo.getEstadoVoo().getSigla());
            retorno.put("aeroporto_origem", converterAeroportoParaJson(voo.getAeroportoOrigem()));
            retorno.put("aeroporto_destino", converterAeroportoParaJson(voo.getAeroportoDestino()));
            return ResponseEntity.ok(retorno);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
   @DeleteMapping("/{codigo}")
   public ResponseEntity<?> removerVoo(@PathVariable Long codigo){
        try {
            vooService.removerVoos(codigo);
            return  ResponseEntity.noContent().build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body("Erro ao deletar");
        }
   }
    @GetMapping
    public ResponseEntity<?> buscarVoosPorAeroportos(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam("origem") String codigoAeroportoOrigem,
            @RequestParam("destino") String codigoAeroportoDestino) {

        Optional<Aeroporto> aeroportoOrigemOptional = aeroportoRepository.findByCodigo(codigoAeroportoOrigem);
        Optional<Aeroporto> aeroportoDestinoOptional = aeroportoRepository.findByCodigo(codigoAeroportoDestino);


        if (aeroportoOrigemOptional.isEmpty() || aeroportoDestinoOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Aeroporto de origem ou destino inválido.");
        }

        Aeroporto aeroportoOrigem = aeroportoOrigemOptional.get();
        Aeroporto aeroportoDestino = aeroportoDestinoOptional.get();

        List<Voos> voosEncontrados = vooService.buscarVoosPorDataOrigemDestino(data, aeroportoOrigem, aeroportoDestino);

        OffsetDateTime dataConsultaFusoLocal = data.atStartOfDay(ZoneOffset.of("-03:00")).toOffsetDateTime();

        List<Object> listaVoosResposta = voosEncontrados.stream().map(voo -> {
            return new HashMap<String, Object>() {{
                put("codigo", voo.getCodigo());
                put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString().replace("Z", "-03:00"));
                put("valor_passagem", voo.getValorPassagem());
                put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
                put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
                put("estado", voo.getEstadoVoo().getSigla());
                put("aeroporto_origem", converterAeroportoParaJson(voo.getAeroportoOrigem()));
                put("aeroporto_destino", converterAeroportoParaJson(voo.getAeroportoDestino()));
            }};
        }).collect(Collectors.toList());

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("data", dataConsultaFusoLocal.toString().replace("Z", "-03:00"));
        resposta.put("origem", aeroportoOrigem.getCodigo());
        resposta.put("destino", aeroportoDestino.getCodigo());
        resposta.put("voos", listaVoosResposta);

        return ResponseEntity.ok(resposta);
    }


    private Map<String, String> converterAeroportoParaJson(Aeroporto aeroporto) {
        return new HashMap<String, String>() {{
            put("codigo", aeroporto.getCodigo());
            put("nome", aeroporto.getNome());
            put("cidade", aeroporto.getCidade());
            put("uf", aeroporto.getUf());
        }};
    }

    @GetMapping(params = {"data", "data-fim"})
    public ResponseEntity<?> buscarVoosPorIntervalo(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicioParam,
            @RequestParam("data-fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFimParam) {

        LocalDateTime dataInicio = dataInicioParam.atStartOfDay();
        LocalDateTime dataFim = dataFimParam.atTime(LocalTime.MAX);

        List<Voos> voosEncontrados = vooService.buscarVoosPorIntervaloDeDatas(dataInicio, dataFim);

        List<Object> listaVoosResposta = voosEncontrados.stream().map(voo -> {
            return new HashMap<String, Object>() {{
                put("codigo", voo.getCodigo());
                put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString().replace("Z", "-03:00"));
                put("valor_passagem", voo.getValorPassagem());
                put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
                put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
                put("estado", voo.getEstadoVoo().getSigla());
                put("aeroporto_origem", converterAeroportoParaJson(voo.getAeroportoOrigem()));
                put("aeroporto_destino", converterAeroportoParaJson(voo.getAeroportoDestino()));
            }};
        }).collect(Collectors.toList());

        return ResponseEntity.ok(listaVoosResposta);
    }
    @PatchMapping("/{codigoVoo}/estado")
    public ResponseEntity<?> cancelarVoo(@PathVariable Long codigoVoo, @RequestBody Map<String, String> payload) {
        if (payload == null || !payload.containsKey("estado") || !payload.get("estado").equalsIgnoreCase("CANCELADO")) {
            return ResponseEntity.badRequest().body("JSON de requisição inválido. Esperado: { \"estado\": \"CANCELADO\" }");
        }

        return vooService.cancelarVoo(codigoVoo);
    }
}



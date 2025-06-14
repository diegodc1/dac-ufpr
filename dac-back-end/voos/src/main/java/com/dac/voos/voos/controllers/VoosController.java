package com.dac.voos.voos.controllers;

import com.dac.voos.voos.dto.VooDTO;
import com.dac.voos.voos.entitys.Aeroporto;
import com.dac.voos.voos.entitys.EstadoVoo;
import com.dac.voos.voos.entitys.Voos;
import com.dac.voos.voos.repositorys.AeroportoRepository;
import com.dac.voos.voos.repositorys.EstadoVooRepository;
import com.dac.voos.voos.services.VooService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
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
        Map<String, Object> retorno = this.converterVooParaJsonRespostaPadrao(novoVoo);
        return new ResponseEntity<>(retorno, HttpStatus.CREATED);
    }


   @GetMapping
   public ResponseEntity<List<Map<String, Object>>> todosVoos(){
        List<Voos> voos = vooService.listVoos();
        List<Map<String, Object>> voosFormados = voos.stream().
                map(this::converterVooParaJsonRespostaPadrao).
                collect(Collectors.toList());
        return ResponseEntity.ok(voosFormados);
   }
    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarCodigo(@PathVariable Long codigo) {
        Optional<Voos> voosOptional = vooService.listVoosCod(codigo);

        if (voosOptional.isPresent()) {
            Voos voo = voosOptional.get();
            Map<String, Object> retorno = converterVooParaJsonRespostaPadrao(voo);
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
    @GetMapping(params = {"data", "origem", "destino"})
    public ResponseEntity<?> buscarVoosPorAeroportos(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataConsulta,
            @RequestParam("origem") String codigoAeroportoOrigem,
            @RequestParam("destino") String codigoAeroportoDestino) {

        Optional<Aeroporto> aeroportoOrigemOptional = aeroportoRepository.findByCodigo(codigoAeroportoOrigem);
        Optional<Aeroporto> aeroportoDestinoOptional = aeroportoRepository.findByCodigo(codigoAeroportoDestino);

        if (aeroportoOrigemOptional.isEmpty() || aeroportoDestinoOptional.isEmpty()) {
            Map<String, String> erroResponce = new HashMap<>();
            erroResponce.put("mensage", "Aeroporto de origem ou destino invalido");
            return  ResponseEntity.badRequest().body(erroResponce);
        }

        Aeroporto aeroportoOrigem = aeroportoOrigemOptional.get();
        Aeroporto aeroportoDestino = aeroportoDestinoOptional.get();

        List<Voos> voosEncontrados = vooService.buscarVoosPorDataOrigemDestino(dataConsulta.toLocalDate(), aeroportoOrigem, aeroportoDestino);

        OffsetDateTime dataHoraAtualConsulta = OffsetDateTime.now(ZoneOffset.of("-03:00"));
        List<Object> listaVoosResposta = voosEncontrados.stream().
                map(this::converterVooParaJsonRespostaPadrao).collect(Collectors.toList());

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("data", dataHoraAtualConsulta.toString());
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

    private Map<String, Object> converterVooParaJsonRespostaPadrao(Voos voo) {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("codigo", voo.getCodigo());
        jsonResponse.put("data", voo.getData_hora().withOffsetSameInstant(ZoneOffset.of("-03:00")).toString());
        jsonResponse.put("valor_passagem", voo.getValorPassagem());
        jsonResponse.put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
        jsonResponse.put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
        jsonResponse.put("estado", voo.getEstadoVoo().getSigla());
        jsonResponse.put("aeroporto_origem", converterAeroportoParaJson(voo.getAeroportoOrigem()));
        jsonResponse.put("aeroporto_destino", converterAeroportoParaJson(voo.getAeroportoDestino()));
        return jsonResponse;
    }


    @GetMapping(params = {"inicio", "fim"})
    public ResponseEntity<Map<String, Object>> buscarVoosPorIntervalo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicioParam,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFimParam,
            HttpEntity<Object> httpEntity) {

        LocalDateTime dataInicio = dataInicioParam.atStartOfDay();
        LocalDateTime dataFim = dataFimParam.atTime(LocalTime.MAX);

        List<Voos> voosEncontrados = vooService.buscarVoosPorIntervaloDeDatas(dataInicio, dataFim);

        List<Map<String, Object>> listaVoodResposta = voosEncontrados.stream().
                map(this:: converterVooParaJsonRespostaPadrao).collect(Collectors.toList());


//        List<Object> listaVoosResposta = voosEncontrados.stream().map(voo -> {
//            return new HashMap<String, Object>() {{
//                put("codigo", voo.getCodigo());
//                put("data", voo.getData_hora().atOffset(ZoneOffset.UTC).toString().replace("Z", "-03:00"));
//                put("valor_passagem", voo.getValorPassagem());
//                put("quantidade_poltronas_total", voo.getQuantidadePoltronasTotal());
//                put("quantidade_poltronas_ocupadas", voo.getQuantidadePoltronasOculpadas());
//                put("estado", voo.getEstadoVoo().getSigla());
//                put("aeroporto_origem", converterAeroportoParaJson(voo.getAeroportoOrigem()));
//                put("aeroporto_destino", converterAeroportoParaJson(voo.getAeroportoDestino()));
//            }};
//        }).collect(Collectors.toList());

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("inicio", dataInicioParam.toString());
        resposta.put("fim", dataFimParam.toString());
        resposta.put("voos", listaVoodResposta);

        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{codigoVoo}/estado")
    public ResponseEntity<?> alterarEstadoVoo(@PathVariable Long codigoVoo, @RequestBody Map<String, String> payload) {
        if (payload == null || !payload.containsKey("estado")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", "JSON de requisição inválido. Esperado: { \"estado\": \"NOVO_ESTADO\" }");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String novoEstadoSigla = payload.get("estado").toUpperCase();

        if (!"CANCELADO".equals(novoEstadoSigla) && !"REALIZADO".equals(novoEstadoSigla)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", "Estado inválido. Valores permitidos: \"CANCELADO\" ou \"REALIZADO\".");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return vooService.atualizarEstadoVoo(codigoVoo, novoEstadoSigla);
    }

    @GetMapping("/admin/db-status")
    public ResponseEntity<Map<String, Object>> getDbStatus() {
        Map<String, Object> dbStatus = new HashMap<>();
        try {
            List<Aeroporto> aeroportos = aeroportoRepository.findAll();
            List<EstadoVoo> estadosVoo = estadoVooRepository.findAll();
            List<Voos> voos = vooService.listVoos();

            dbStatus.put("aeroportos", aeroportos.stream()
                    .map(this::converterAeroportoParaJson)
                    .collect(Collectors.toList()));
            dbStatus.put("estados_voo", estadosVoo.stream()
                    .map(estado -> new HashMap<String, String>() {{
                        put("sigla", estado.getSigla());
                        put("descricao", estado.getDescricao());
                    }})
                    .collect(Collectors.toList()));
            dbStatus.put("voos", voos.stream()
                    .map(this::converterVooParaJsonRespostaPadrao)
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(dbStatus);

        } catch (Exception e) {
            logger.error("Erro ao obter status do banco de dados: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensagem", "Erro ao acessar o banco de dados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}



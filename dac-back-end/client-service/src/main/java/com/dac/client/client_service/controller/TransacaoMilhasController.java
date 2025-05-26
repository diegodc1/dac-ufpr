package com.dac.client.client_service.controller;
import java.util.List;
import com.dac.client.client_service.dto.CompraMilhasDTO;
import com.dac.client.client_service.dto.TransacaoMilhasDTO;
import com.dac.client.client_service.model.TransacaoMilhas;
import com.dac.client.client_service.service.TransacaoMilhasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/TransacaoMilhas")
public class TransacaoMilhasController {
    @Autowired
    private TransacaoMilhasService transacaoMilhasService;

    @GetMapping("/{email}/extract")
    public ResponseEntity<List<TransacaoMilhasDTO>> getExtratoMilhas(@PathVariable String email) {
        try {
            List<TransacaoMilhas> extratoEntities = transacaoMilhasService.getExtratoMilhas(email);
            List<TransacaoMilhasDTO> extratoDTOs = extratoEntities.stream()
                    .map(this::toTransacaoMilhasDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(extratoDTOs);
        } catch (RuntimeException e) {
            System.err.println("Erro ao buscar extrato de milhas para codigo " + email + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar extrato de milhas para o codigo :  " + email + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{email}/buy")
    public ResponseEntity<TransacaoMilhasDTO> comprarMilhas(@PathVariable String email, @RequestBody CompraMilhasDTO purchaseRequest) {
        try {
            TransacaoMilhas transacaoRegistrada = transacaoMilhasService.registrarEntradaMilhas(
                    email,
                    purchaseRequest.getQuantidade(),
                    purchaseRequest.getValorPago(),
                    "COMPRA DE MILHAS"
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(toTransacaoMilhasDTO(transacaoRegistrada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private TransacaoMilhasDTO toTransacaoMilhasDTO(TransacaoMilhas transacao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = transacao.getDataHora().format(formatter);

        String reservationCode = transacao.getCodigoReserva() != null ? transacao.getCodigoReserva() : "";

        String amountReais = transacao.getValorEmReais() != null ?
                String.format("R$%.2f", transacao.getValorEmReais().doubleValue()).replace('.', ',') : "R$0,00";

        return new TransacaoMilhasDTO(
                formattedDate,
                reservationCode,
                amountReais,
                transacao.getQuantidade().intValue(),
                transacao.getDescricao(),
                transacao.getTipo()
        );
    }

}

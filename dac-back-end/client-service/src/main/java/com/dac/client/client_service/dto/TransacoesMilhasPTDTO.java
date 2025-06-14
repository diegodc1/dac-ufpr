package com.dac.client.client_service.dto;

import com.dac.client.client_service.model.TransacaoMilhas;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacoesMilhasPTDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime data;
    private Double valor_reais;
    private Long quantidade_milhas;
    private String descricao;
    private String codigo_reserva;
    private String tipo;

    public TransacoesMilhasPTDTO(TransacaoMilhas transacaoMilhas) {
        this.data = transacaoMilhas.getDataHora();
        this.valor_reais = transacaoMilhas.getValor_reais();
        this.quantidade_milhas = transacaoMilhas.getQuantidade();
        this.descricao = transacaoMilhas.getDescricao();
        this.codigo_reserva = transacaoMilhas.getCodigoReserva();
        this.tipo = transacaoMilhas.getTipo();
    }
}

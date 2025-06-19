package com.example.reservas.entity;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Usando UUID
    @Column(name = "id_reserva", nullable = false, updatable = false)
    private UUID idReserva;

    @NotNull(message = "O código da reserva não pode ser nulo.")
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull(message = "O código do voo não pode ser nulo.")
    @Column(name = "codigo_voo", nullable = false)
    private String codigoVoo;

    @NotNull(message = "A data da reserva não pode ser nula.")
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @NotNull(message = "O valor da reserva não pode ser nulo.")
    @Positive(message = "O valor da reserva deve ser positivo.")
    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Positive(message = "O número de milhas utilizadas deve ser positivo.")
    @Column(name = "milhas_utilizadas")
    private int milhasUtilizadas;

    @Positive(message = "A quantidade de poltronas deve ser positiva.")
    @Column(name = "quantidade_poltronas")
    private int quantidadePoltronas;

    @Column(name = "codigo_cliente")
    private Integer codigoCliente;

    @NotNull(message = "O ID da transação não pode ser nulo.")
    @Column(name = "id_transacao", nullable = false)
    private UUID idTransacao;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // Cascade aqui
    @JoinColumn(name = "aeroporto_origem_id", referencedColumnName = "id_aeroporto")
    private AeroportoEntity aeroportoOrigem;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // Cascade aqui
    @JoinColumn(name = "aeroporto_destino_id", referencedColumnName = "id_aeroporto")
    private AeroportoEntity aeroportoDestino;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // Cascade aqui
    @JoinColumn(name = "estado_id", referencedColumnName = "id_estado")
    private EstadoReservaEntity descricao;

    @OneToMany(mappedBy = "reserva", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoEstatusEntity> historicosEstatus;

    // Construtor com parâmetros
    public ReservaEntity(String codigo, String codigoVoo, ZonedDateTime data, BigDecimal valor,
                         int milhasUtilizadas, int quantidadePoltronas, Integer codigoCliente,
                         UUID idTransacao, AeroportoEntity aeroportoOrigem, AeroportoEntity aeroportoDestino,
                         EstadoReservaEntity estado) {
        this.codigo = codigo;
        this.codigoVoo = codigoVoo;
        this.data = data;
        this.valor = valor;
        this.milhasUtilizadas = milhasUtilizadas;
        this.quantidadePoltronas = quantidadePoltronas;
        this.codigoCliente = codigoCliente;
        this.idTransacao = idTransacao;
        this.aeroportoOrigem = aeroportoOrigem;
        this.aeroportoDestino = aeroportoDestino;
        this.descricao = estado;
    }
}

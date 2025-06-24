package com.example.reservas.configs;

import com.example.reservas.model.Aeroporto;
import com.example.reservas.model.EstadoReserva;
import com.example.reservas.model.Reserva;
import com.example.reservas.repositorys.AeroportoRepository;
import com.example.reservas.repositorys.StatusReservaRepository;
import com.example.reservas.repositorys.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Component
public class BDInitializer implements CommandLineRunner {

    private final StatusReservaRepository statusReservaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;


    // Construtor com injeção do repositório
    BDInitializer(StatusReservaRepository statusReservaRepository) {
        this.statusReservaRepository = statusReservaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Inserir os estados no banco, se não existirem
        insertEstadoIfNotExists(1, "CFD", "CONFIRMADA");
        insertEstadoIfNotExists(2, "CKN", "CHECK-IN");
        insertEstadoIfNotExists(3, "CLD", "CANCELADA");
        insertEstadoIfNotExists(4, "EBD", "EMBARCADA");
        insertEstadoIfNotExists(5, "RZD", "REALIZADO");

        // Inserir os aeroportos de origem e destino, se não existirem
        insertAeroportoIfNotExists("GRU", "Aeroporto Internacional de São Paulo", "São Paulo", "SP");
        insertAeroportoIfNotExists("GIG", "Aeroporto Internacional do Rio de Janeiro", "Rio de Janeiro", "RJ");

        // Verificar se a reserva já existe
        Optional<Reserva> existingReserva = reservaRepository.getByCodigo("RES001");

        if (existingReserva.isEmpty()) {
            // Buscar os aeroportos e o estado no banco
            Optional<Aeroporto> aeroportoOrigemOpt = aeroportoRepository.findById("GRU");
            Optional<Aeroporto> aeroportoDestinoOpt = aeroportoRepository.findById("GIG");
          Optional<EstadoReserva> estadoOpt = Optional.ofNullable(statusReservaRepository.findByDescricaoEstado("CONFIRMADA"));


            if (aeroportoOrigemOpt.isPresent() && aeroportoDestinoOpt.isPresent() && estadoOpt.isPresent()) {
                Aeroporto aeroportoOrigem = aeroportoOrigemOpt.get();
                Aeroporto aeroportoDestino = aeroportoDestinoOpt.get();
                EstadoReserva estado = estadoOpt.get();

                // Convertendo a string de data para ZonedDateTime
                String dataString = "2025-06-21T10:00:00Z"; // A string que você possui
                ZonedDateTime dataReserva = ZonedDateTime.parse(dataString, DateTimeFormatter.ISO_DATE_TIME);

                // Criar e salvar a reserva
                Reserva novaReserva = Reserva.builder()
                        .codigo("RES001")
                        .codigoVoo("TADS0001")
                        .data(dataReserva)  // Passando o ZonedDateTime para o método data
                        .estado(estado)
                        .valor(BigDecimal.valueOf(250.00))
                        .milhasUtilizadas(50)
                        .quantidadePoltronas(1)
                        .codigoCliente(1)  // código do cliente
                        .idTransacao(UUID.randomUUID())  // Gerar id transação único
                        .aeroportoOrigem(aeroportoOrigem)
                        .aeroportoDestino(aeroportoDestino)
                        .build();

                reservaRepository.save(novaReserva);
                System.out.println("Reserva criada com sucesso!");
            } else {
                System.out.println("Erro: Não foi possível encontrar os aeroportos ou o estado.");
            }
        } else {
            System.out.println("A reserva já existe no banco de dados.");
        }
    }

    // Método para inserir os estados, caso não existam
   
  private void insertEstadoIfNotExists(int codigoEstado, String acronimo, String descricao) {
    // Verifica se o estado já existe no banco de dados
    EstadoReserva estadoExistente = statusReservaRepository.findByCodigoEstado(codigoEstado);
    
    if (estadoExistente == null) {  // Se não encontrar o estado, inserimos um novo
        // Criando o novo estado com o builder
        EstadoReserva novoEstado = EstadoReserva.builder()
                .codigoEstado(codigoEstado)    // Usando o builder para setar o código
                .acronimoEstado(acronimo)      // Usando o builder para setar o acrônimo
                .descricaoEstado(descricao)    // Usando o builder para setar a descrição
                .build();                     // O ID será gerado automaticamente pelo banco
        
        // Salvando o estado no repositório
        statusReservaRepository.save(novoEstado);
        System.out.println("Estado " + descricao + " inserido.");
    } else {
        System.out.println("Estado " + descricao + " já existe.");
    }
}



    // Método para inserir os aeroportos, caso não existam
    private void insertAeroportoIfNotExists(String codigo, String nome, String cidade, String uf) {
        Optional<Aeroporto> aeroportoExistente = aeroportoRepository.findById(codigo);
        if (aeroportoExistente.isEmpty()) {
            Aeroporto novoAeroporto = new Aeroporto(codigo, nome, cidade, uf);
            aeroportoRepository.save(novoAeroporto);
            System.out.println("Aeroporto " + nome + " inserido.");
        }
    }
}

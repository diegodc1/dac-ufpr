CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de estados
CREATE TABLE IF NOT EXISTS tb_estado (
                                         id_estado UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo_estado INTEGER NOT NULL UNIQUE,
    acronimo_estado VARCHAR(3) NOT NULL UNIQUE,
    desc_estado VARCHAR(15) NOT NULL UNIQUE
    );

INSERT INTO tb_estado (codigo_estado, acronimo_estado, desc_estado)
VALUES
    (1, 'CFD', 'CONFIRMADA'),
    (2, 'CKN', 'CHECK-IN'),
    (3, 'CLD', 'CANCELADO'),
    (4, 'EBD', 'EMBARCADA'),
    (5, 'RZD', 'REALIZADO'),
    (6, 'CRD', 'CRIADA')
    ON CONFLICT (codigo_estado) DO NOTHING;


CREATE TABLE IF NOT EXISTS tb_aeroporto (
                                            codigo VARCHAR(3) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    uf VARCHAR(20) NOT NULL
    );

-- Tabela de reservas
CREATE TABLE IF NOT EXISTS tb_reserva (
                                          id_reserva UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo VARCHAR(6) NOT NULL UNIQUE,
    codigo_voo VARCHAR(8) NOT NULL,
    data TIMESTAMP WITH TIME ZONE NOT NULL,
                       estado UUID NOT NULL,
                       valor NUMERIC(10, 2) NOT NULL,
    milhas_utilizadas INTEGER NOT NULL,
    quantidade_poltronas INTEGER NOT NULL,
    codigo_cliente VARCHAR(24) NOT NULL,
    id_transacao UUID UNIQUE,
    aeroporto_origem VARCHAR(3),
    aeroporto_destino VARCHAR(3),
    CONSTRAINT fk_estado_reserva FOREIGN KEY (estado) REFERENCES tb_estado (id_estado)
    );

-- Tabela de trocas
CREATE TABLE IF NOT EXISTS tb_troca (
                                        id_troca UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    data_troca TIMESTAMP WITH TIME ZONE NOT NULL,
                             reserva UUID NOT NULL,
                             estado_inicial UUID NOT NULL,
                             estado_final UUID NOT NULL,
                             CONSTRAINT fk_reserva FOREIGN KEY (reserva) REFERENCES tb_reserva (id_reserva),
    CONSTRAINT fk_estado_inicial FOREIGN KEY (estado_inicial) REFERENCES tb_estado (id_estado),
    CONSTRAINT fk_estado_final FOREIGN KEY (estado_final) REFERENCES tb_estado (id_estado)
    );

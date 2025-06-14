-- Habilita a extensão para UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ================================
-- TABELA: tb_estado
-- ================================
CREATE TABLE IF NOT EXISTS tb_estado (
    id_estado UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo_estado INTEGER NOT NULL UNIQUE,
    acronim_estado VARCHAR(3) NOT NULL UNIQUE,
    descricao_estado VARCHAR(15) NOT NULL UNIQUE
);

-- Inserts de estados
INSERT INTO tb_estado (codigo_estado, acronim_estado, descricao_estado)
VALUES
    (1, 'CFD', 'CONFIRMADA'),
    (2, 'CKN', 'CHECK-IN'),
    (3, 'CLD', 'CANCELADA'),
    (4, 'EBD', 'EMBARCADA'),
    (5, 'RZD', 'REALIZADA')
ON CONFLICT (codigo_estado) DO NOTHING;

-- ================================
-- TABELA: tb_aeroporto
-- ================================
CREATE TABLE IF NOT EXISTS tb_aeroporto (
    codigo VARCHAR(3) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    uf VARCHAR(20) NOT NULL
);

-- Inserts de aeroportos
INSERT INTO tb_aeroporto (codigo, nome, cidade, uf)
VALUES
    ('GRU', 'Aeroporto Internacional de Guarulhos', 'São Paulo', 'SP'),
    ('GIG', 'Aeroporto Internacional do Galeão', 'Rio de Janeiro', 'RJ')
ON CONFLICT (codigo) DO NOTHING;

-- ================================
-- TABELA: tb_reserva
-- ================================
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
    id_transacao UUID NOT NULL UNIQUE,
    aeroporto_origem VARCHAR(3) NOT NULL,
    aeroporto_destino VARCHAR(3) NOT NULL,
    CONSTRAINT fk_estado_reserva FOREIGN KEY (estado) REFERENCES tb_estado (id_estado),
    CONSTRAINT fk_aeroporto_origem FOREIGN KEY (aeroporto_origem) REFERENCES tb_aeroporto (codigo),
    CONSTRAINT fk_aeroporto_destino FOREIGN KEY (aeroporto_destino) REFERENCES tb_aeroporto (codigo)
);

-- ================================
-- TABELA: tb_historico_estatus
-- ================================
CREATE TABLE IF NOT EXISTS tb_historico_estatus (
    id_historico UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    data_alt_estado TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reserva UUID NOT NULL,
    estado_inicial UUID NOT NULL,
    estado_final UUID NOT NULL,
    CONSTRAINT fk_reserva FOREIGN KEY (reserva) REFERENCES tb_reserva (id_reserva),
    CONSTRAINT fk_estado_inicial FOREIGN KEY (estado_inicial) REFERENCES tb_estado (id_estado),
    CONSTRAINT fk_estado_final FOREIGN KEY (estado_final) REFERENCES tb_estado (id_estado)
);

-- ================================
-- DADO DE TESTE: Reserva
-- ================================
WITH ins_reserva AS (
    INSERT INTO tb_reserva (
        codigo,
        codigo_voo,
        data,
        estado,
        valor,
        milhas_utilizadas,
        quantidade_poltronas,
        codigo_cliente,
        id_transacao,
        aeroporto_origem,
        aeroporto_destino
    )
    VALUES (
        'ABC123',
        'TADS0001',
        '2024-10-10T14:30:00-03:00',
        (SELECT id_estado FROM tb_estado WHERE descricao_estado = 'CONFIRMADA'),
        250.00,
        50,
        1,
        'cliente123',
        uuid_generate_v4(),
        'GRU',
        'GIG'
    )
    RETURNING id_reserva
)
-- Inserção no histórico de status
INSERT INTO tb_historico_estatus (
    reserva,
    estado_inicial,
    estado_final
)
SELECT
    id_reserva,
    (SELECT id_estado FROM tb_estado WHERE descricao_estado = 'CONFIRMADA'),
    (SELECT id_estado FROM tb_estado WHERE descricao_estado = 'CHECK-IN')
FROM ins_reserva;

-- =========================================
-- 1. Criar extensão para uso de UUID
-- =========================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================================
-- 2. Criar tabela de estados
-- =========================================
CREATE TABLE IF NOT EXISTS tb_estado (
    id_estado UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo_estado INTEGER NOT NULL UNIQUE,
    acronim_estado VARCHAR(3) NOT NULL UNIQUE,
    descricao_estado VARCHAR(15) NOT NULL UNIQUE
);

-- =========================================
-- 3. Criar tabela de aeroportos
-- =========================================
CREATE TABLE IF NOT EXISTS tb_aeroporto (
    codigo VARCHAR(3) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    uf VARCHAR(20) NOT NULL
);

-- =========================================
-- 4. Criar tabela de reservas
-- =========================================
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

-- =========================================
-- 5. Criar tabela de histórico de status
-- =========================================
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

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS tabela_funcionario (
        id_funcionario BIGSERIAL PRIMARY KEY,
        id_usuario VARCHAR(24) NOT NULL UNIQUE,
        nome VARCHAR(50) NOT NULL,
        cpf VARCHAR(11) NOT NULL UNIQUE,
        email VARCHAR(50) NOT NULL UNIQUE,
        numero_telefone VARCHAR(11) NOT NULL,
        estado VARCHAR(8) NOT NULL
    );
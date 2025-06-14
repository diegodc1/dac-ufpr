CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS tabela_funcionario (
    id_funcionario BIGSERIAL PRIMARY KEY,
    id_usuario VARCHAR(24) NOT NULL UNIQUE,
    nome VARCHAR(50) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    numero_telefone VARCHAR(11) NOT NULL UNIQUE,
    estado VARCHAR(8) NOT NULL
);

INSERT INTO tabela_funcionario (
    id_usuario,
    nome,
    cpf,
    email,
    numero_telefone,
    estado
) VALUES (
     '1',
     'Funcionario Padrão',
     '90769281001',
     'func_pre@gmail.com',
     '41999999999',
     'PR'
 )
ON CONFLICT (cpf) DO NOTHING;

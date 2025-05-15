CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE
    IF NOT EXISTS tb_status (
        id_status UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
        cod_status INTEGER NOT NULL UNIQUE,
        acronim_status VARCHAR(3) NOT NULL UNIQUE,
        descricao_status VARCHAR(15) NOT NULL UNIQUE
    );

INSERT INTO
    tb_status (cod_status, acronim_status, descricao_status)
VALUES
    (1, 'RVD', 'RESERVADO'),
    (2, 'CKN', 'CHECK-IN'),
    (3, 'CLD', 'CANCELADO'),
    (4, 'EBD', 'EMBARCANDO'),
    (5, 'CPD', 'COMPLETADO'),
    (6, 'NCP', 'NÃO COMPLETADO');

CREATE TABLE IF NOT EXISTS tb_reserva (
    id_reserva UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    cod_reserva VARCHAR(6) NOT NULL UNIQUE, 
    code_voo VARCHAR(8) NOT NULL,
    date_reserva TIMESTAMP WITH TIME ZONE NOT NULL,
    status_reserva UUID NOT NULL,
    dinheiro_gastos NUMERIC(10, 2) NOT NULL, 
    milhas_gastas INTEGER NOT NULL,
    numero_assento INTEGER NOT NULL,
    id_usuario VARCHAR(24) NOT NULL,
    id_transacao UUID NOT NULL UNIQUE,
    CONSTRAINT fk_status_reserva FOREIGN KEY (status_reserva) REFERENCES tb_status (id_status)
);


CREATE TABLE IF NOT EXISTS  tb_troca (
    id_troca UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    data_troca TIMESTAMP WITH TIME ZONE NOT NULL,
    reserva UUID NOT NULL,
    status_inicial UUID NOT NULL,
    status_final UUID NOT NULL,
    CONSTRAINT fk_reserva FOREIGN KEY (reserva) REFERENCES tb_reserva (id_reserva),
    CONSTRAINT fk_status_inicial FOREIGN KEY (status_inicial) REFERENCES tb_status (id_status),
    CONSTRAINT fk_status_final FOREIGN KEY (status_final) REFERENCES tb_status (id_status)
);
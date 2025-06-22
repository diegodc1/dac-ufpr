
-- Inserção de estados (já está feito, mas só para garantir que tenha os estados no banco)
INSERT INTO tb_estado (codigo_estado, acronim_estado, descricao_estado)
VALUES
    (1, 'CFD', 'CONFIRMADA'),
    (2, 'CKN', 'CHECK-IN'),
    (3, 'CLD', 'CANCELADO'), 
    (4, 'EBD', 'EMBARCADA'),
    (5, 'RZD', 'REALIZADO')
ON CONFLICT (codigo_estado) DO NOTHING;

-- Inserção dos aeroportos de origem e destino (caso não existam)
INSERT INTO tb_aeroporto (codigo, nome, cidade, uf)
VALUES
    ('GRU', 'Aeroporto Internacional de São Paulo', 'São Paulo', 'SP'),
    ('GIG', 'Aeroporto Internacional do Rio de Janeiro', 'Rio de Janeiro', 'RJ')
ON CONFLICT (codigo) DO NOTHING;

-- Inserção de uma reserva (com dados do teste fornecido)
INSERT INTO tb_reserva (codigo, codigo_voo, data, estado, valor, milhas_utilizadas, quantidade_poltronas, codigo_cliente, id_transacao, aeroporto_origem, aeroporto_destino)
VALUES
    (
        'RES001',  -- código da reserva
        'TADS0001',  -- código do voo
        '2025-06-21T10:00:00Z',  -- data da reserva (substitua com a data correta)
        (SELECT id_estado FROM tb_estado WHERE codigo_estado = 1),  -- estado CONFIRMADA
        250.00,  -- valor da reserva
        50,  -- milhas utilizadas
        1,  -- quantidade de poltronas
        '1',  -- código cliente (como string, 1 = '1')
        uuid_generate_v4(),  -- id_transacao único
        'GRU',  -- aeroporto de origem
        'GIG'   -- aeroporto de destino
    )
ON CONFLICT (codigo) DO NOTHING;

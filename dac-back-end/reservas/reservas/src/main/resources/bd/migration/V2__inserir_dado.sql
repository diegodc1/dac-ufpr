-- =========================================
-- Inserir dados na tabela de estados
-- =========================================
INSERT INTO tb_estado (codigo_estado, acronim_estado, descricao_estado)
VALUES
    (1, 'CFD', 'CONFIRMADA'),
    (2, 'CKN', 'CHECK-IN'),
    (3, 'CLD', 'CANCELADA'),
    (4, 'EBD', 'EMBARCADA'),
    (5, 'RZD', 'REALIZADA')
ON CONFLICT (codigo_estado) DO NOTHING;

-- =========================================
-- Inserir dados na tabela de aeroportos
-- =========================================
INSERT INTO tb_aeroporto (codigo, nome, cidade, uf)
VALUES
    ('GRU', 'Aeroporto Internacional de Guarulhos', 'São Paulo', 'SP'),
    ('GIG', 'Aeroporto Internacional do Galeão', 'Rio de Janeiro', 'RJ')
ON CONFLICT (codigo) DO NOTHING;

-- =========================================
-- Inserir dados na tabela de reservas
-- =========================================
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
    (SELECT id_estado FROM tb_estado WHERE descricao_estado = 'CONFIRMADA' LIMIT 1),
    250.00,
    50,
    1,
    'cliente123',
    uuid_generate_v4(),
    'GRU',
    'GIG'
)
ON CONFLICT (codigo) DO NOTHING;

-- =========================================
-- Inserir dados na tabela de histórico de status
-- =========================================
INSERT INTO tb_historico_estatus (reserva, estado_inicial, estado_final)
SELECT
    id_reserva,
    (SELECT id_estado FROM tb_estado WHERE descricao_estado = 'CONFIRMADA' LIMIT 1),
    (SELECT id_estado FROM tb_estado WHERE descricao_estado = 'CHECK-IN' LIMIT 1)
FROM tb_reserva
WHERE codigo = 'ABC123';

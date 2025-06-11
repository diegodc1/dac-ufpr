
INSERT INTO aeroporto (cod_aero, nome_aero, cidade, uf) VALUES
        ('GRU', 'Aeroporto Internacional de São Paulo/Guarulhos', 'Guarulhos', 'SP'),
        ('GIG', 'Aeroporto Internacional do Rio de Janeiro/Galeão', 'Rio de Janeiro', 'RJ'),
        ('CWB', 'Aeroporto Internacional de Curitiba', 'Curitiba', 'PR'),
        ('POA', 'Aeroporto Internacional Salgado Filho', 'Porto Alegre', 'RS')
    ON CONFLICT (cod_aero) DO NOTHING;

INSERT INTO estado_voo (sigla_estado, descricao) VALUES
     ('CONFIRMADO', 'CONFIRMADO'),
     ('CANCELADO', 'CANCELADO'),
     ('REALIZADO', 'REALIZADO')
    ON CONFLICT (descricao) DO NOTHING;

-- Assumindo que estado_voo_id 1 existe e refere-se a 'CONFIRMADO'
INSERT INTO voos (
    data_hora,
    cod_aero_origem,
    cod_aero_destino,
    valor_passagem,
    qtd_poltrona_total,
    qtd_poltrona_oculpada,
    estado_voo_id
) VALUES
      ('2025-08-10T10:30:00', 'POA', 'CWB', 450.00, 180, 75, 1),
      ('2025-09-11T09:30:00', 'CWB', 'GIG', 520.00, 200, 90, 1),
      ('2025-10-12T08:30:00', 'CWB', 'POA', 400.00, 150, 50, 1),
      ('2025-07-01T10:00:00', 'POA', 'CWB', 360.00, 100, 20, 1)
    ON CONFLICT DO NOTHING;

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

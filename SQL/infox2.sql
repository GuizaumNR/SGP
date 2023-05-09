use dbinfox;

CREATE TABLE usuarios (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    login VARCHAR(15) UNIQUE NOT NULL,
    senha VARCHAR(60) NOT NULL,
    tipo ENUM('admin', 'consulta') NOT NULL DEFAULT 'consulta'
);

CREATE TABLE fornecedores (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(50),
    endereco VARCHAR(100)
);

CREATE TABLE animais (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100),
    raca VARCHAR(100) NOT NULL,
    idade ENUM('terneiro', 'novilho', 'vaca_velha') NOT NULL,
    sexo ENUM('boi', 'touro_reprodutor', 'femea') NOT NULL,
    id_fornecedor INT NOT NULL,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id) ON DELETE CASCADE
);

CREATE TABLE transacoes (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_transacao ENUM('compra', 'venda') NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    id_animal INT NOT NULL,
    id_fornecedor INT NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animais(id) ON DELETE CASCADE,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id) ON DELETE CASCADE
);

CREATE TABLE lucros (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_lucro DATE NOT NULL
);

CREATE TABLE despesas (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(100) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_despesa DATE NOT NULL
);

CREATE TABLE compras_animais (
    id_compra INT NOT NULL,
    id_animal INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_fornecedor INT NOT NULL,
    valor_peso DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_compra, id_animal),
    FOREIGN KEY (id_compra) REFERENCES transacoes(id) ON DELETE CASCADE,
    FOREIGN KEY (id_animal) REFERENCES animais(id) ON DELETE CASCADE,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id) ON DELETE CASCADE
);
CREATE TABLE vendas_animais (
    id_venda INT NOT NULL,
    id_animal INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_fornecedor INT NOT NULL,
    valor_peso DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_venda, id_animal),
    FOREIGN KEY (id_venda) REFERENCES transacoes(id) ON DELETE CASCADE,
    FOREIGN KEY (id_animal) REFERENCES animais(id) ON DELETE CASCADE,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id) ON DELETE CASCADE
);

-- inserts
INSERT INTO usuarios (login, senha, tipo) VALUES
('joao123', 'senha123', 'admin'),
('maria456', 'senha456', 'consulta');

INSERT INTO fornecedores (nome, telefone, email, endereco) VALUES
('Fornecedor A', '(11) 1111-1111', 'fornecedora@gmail.com', 'Rua A, 123'),
('Fornecedor B', '(22) 2222-2222', 'fornecedorb@gmail.com', 'Rua B, 456');

INSERT INTO animais (descricao, raca, idade, sexo, id_fornecedor) VALUES
('Boi preto', 'Nelore', 'terneiro', 'boi', 1),
('Vaca leiteira', 'Holandesa', 'vaca_velha', 'femea', 2);

INSERT INTO transacoes (tipo_transacao, quantidade, valor_unitario, id_animal, id_fornecedor) VALUES
('compra', 10, 100.00, 1, 1),
('venda', 5, 150.00, 2, 2);

INSERT INTO lucros (descricao, valor, data_lucro) VALUES
('Lucro da venda de animais', 500.00, '2023-03-30'),
('Lucro da venda de leite', 1000.00, '2023-03-28');

INSERT INTO despesas (descricao, valor, data_despesa) VALUES
('Compra de ração', 500.00, '2023-03-25'),
('Pagamento de funcionários', 2000.00, '2023-03-30');

-- INSERTS

INSERT INTO compras_animais (id_compra, id_animal, quantidade, id_fornecedor, valor_peso, valor_total) VALUES
(1, 1, 10, 1, 10.00, 100.00);

INSERT INTO vendas_animais (id_venda, id_animal, quantidade, id_fornecedor, valor_peso, valor_total) VALUES
(1, 2, 5, 2, 15.00, 75.00);

-- Inserir uma transação de venda na tabela de transações
INSERT INTO transacoes (tipo_transacao, quantidade, valor_unitario, id_animal, id_fornecedor)
VALUES ('venda', 10, 5000.00, 1, 1);

-- Inserir detalhes da venda na tabela de vendas_animais, referenciando a transação correspondente
INSERT INTO vendas_animais (id_venda, id_animal, quantidade, id_fornecedor, valor_peso, valor_total)
VALUES (LAST_INSERT_ID(), 1, 10, 1, 500.00, 5000.00);
-- Observe que usamos a função LAST_INSERT_ID() para obter o valor do ID da transação inserida anteriormente e inserimos esse valor 
-- na coluna id_venda na tabela de vendas_animais. Isso garante que a venda esteja associada à transação correspondente na tabela de transações

-- CONSULTAS

select * from usuarios;

-- Obter todos os animais disponíveis para compra:
SELECT a.raca, a.sexo, a.idade, COUNT(*) - SUM(CASE WHEN t.tipo_transacao = 'compra' THEN t.quantidade ELSE -t.quantidade END) AS disponiveis
FROM animais a
LEFT JOIN transacoes t ON a.id = t.id_animal
GROUP BY a.raca, a.sexo, a.idade;


-- Obter todos os fornecedores que já fizeram alguma venda:
SELECT DISTINCT fornecedores.* 
FROM fornecedores 
INNER JOIN compras_animais ON compras_animais.id_fornecedor = fornecedores.id 
INNER JOIN transacoes ON compras_animais.id_compra = transacoes.id
WHERE transacoes.tipo_transacao = 'compra';

-- Obter todas as transações de venda realizadas no mês de março de 2023:
SELECT * FROM transacoes 
WHERE tipo_transacao = 'venda' AND MONTH(data_transacao) = 3 AND YEAR(data_transacao) = 2023;

-- Obter a quantidade de cada animal vendido em todas as vendas realizadas:
SELECT animais.nome, SUM(transacoes.quantidade) as quantidade_total
FROM animais 
INNER JOIN transacoes ON animais.id = transacoes.id_animal 
WHERE transacoes.tipo_transacao = 'venda'
GROUP BY animais.id;

-- Obter o valor total gasto em cada compra realizada:
SELECT compras_animais.id_compra, SUM(valor_total) as valor_total_compra
FROM compras_animais
GROUP BY compras_animais.id_compra;

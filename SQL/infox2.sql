CREATE TABLE usuarios (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome varchar(30) NOT NULL,
    login VARCHAR(15) UNIQUE NOT NULL,
    senha VARCHAR(60) NOT NULL,
    tipo ENUM('admin', 'consulta') NOT NULL DEFAULT 'consulta'
);

CREATE TABLE fornecedores (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(100)
);

CREATE TABLE animais (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	descricao VARCHAR(100) UNIQUE NOT NULL,
    quantidade INT NOT NULL,
    idade ENUM('terneiro', 'novilho', 'vaca_velha') NOT NULL,
    sexo ENUM('boi', 'touro_reprodutor', 'femea') NOT NULL
);



CREATE TABLE compras_animais (
    id_compra INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_compra TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    id_animal INT NOT NULL,
    quantidade INT NOT NULL,
    media_kg DECIMAL(10,2) NOT NULL,
    preco_kg DECIMAL(10, 2) NOT NULL,
    valor_total decimal(10,2) NOT NULL,
    criador VARCHAR(100) NOT NULL,
    pagador ENUM("alemao","negocio","adiantamento"),
    local_compra VARCHAR(100),
    operador VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (id_animal) REFERENCES animais(id)
);



CREATE TABLE vendas_animais (
    id_venda INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_venda TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    id_animal INT NOT NULL,
    quantidade INT NOT NULL,
    media_kg DECIMAL(10,2) NOT NULL,
    preco_kg DECIMAL(10, 2) NOT NULL,
    valor_total decimal(10,2) NOT NULL,
    vendedor VARCHAR(100) NOT NULL,
    comprador VARCHAR(100) NOT NULL,
    local_venda VARCHAR(100) NOT NULL,
    operador VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (id_animal) REFERENCES animais(id)
);

CREATE TABLE movimentacoes (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    tipo ENUM('nascimento', 'morte') NOT NULL,
    data_movimentacao TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    animal_id INT NOT NULL,
    quantidade INT NOT NULL,
    observacao VARCHAR(200),
    operador VARCHAR(100) NOT NULL,
    FOREIGN KEY (animal_id) REFERENCES animais (id)
);

CREATE TABLE lucros (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(200) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_lucro TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    operador VARCHAR(100) NOT NULL
);

CREATE TABLE despesas (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(200) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_despesa TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    operador VARCHAR(100) NOT NULL
);

-- CONSULTAS {
use dbinfox;

select * from usuarios;
select * from animais;
select * from fornecedores;
select * from vendas_animais;


-- relatorio ja formatado de vendas em um intervalo de tempo
SELECT id_venda, DATE_FORMAT(data_venda, '%d/%m/%Y %H:%i:%s') as data_formatada, a.descricao as animal_descricao,
    v.quantidade,
    media_kg,
    preco_kg,
    valor_total,
    vendedor,
    comprador,
    local_venda,
    operador
FROM vendas_animais v
JOIN animais a ON v.id_animal = a.id
WHERE data_venda BETWEEN '2023-07-20' AND '2023-07-31'
ORDER BY data_venda;

-- lsita de animais mais vendidos
SELECT a.descricao AS nome_animal, SUM(va.quantidade) AS total_vendido
FROM vendas_animais va
JOIN animais a ON va.id_animal = a.id
GROUP BY va.id_animal, a.descricao
ORDER BY total_vendido DESC;

-- lista de vendas entre determinadas datas
SELECT *
FROM vendas_animais
WHERE data_venda >= '2023-01-01' AND data_venda <= '2023-07-31';

describe usuarios;
describe animais;
describe compras_animais;
describe vendas_animais;

ALTER TABLE vendas_animais
CHANGE COLUMN local_compra  local_venda VARCHAR(100);
-- }






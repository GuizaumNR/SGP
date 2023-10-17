CREATE DATABASE dbinfox;

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
	descricao VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    idade VARCHAR(100) NOT NULL,
    sexo VARCHAR(100) NOT NULL
);

drop table compras_animais;

CREATE TABLE compras_animais (
    id_compra INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_compra TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    id_animal INT NOT NULL,
    quantidade INT NOT NULL,
    kg_totais DECIMAL(10, 2) NOT NULL,
    media_kg DECIMAL(10,2) NOT NULL,
    preco_kg DECIMAL(10, 2) NOT NULL,
    valor_total decimal(10,2) NOT NULL,
    porce_comissao DECIMAL(10, 2) NOT NULL,
    comissao DECIMAL(10, 2) NOT NULL,
    criador VARCHAR(100) NOT NULL,
    pagador VARCHAR(100) NOT NULL,
    pagamento VARCHAR(100) NOT NULL,
    operador VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (id_animal) REFERENCES animais(id) ON DELETE CASCADE
);

CREATE TABLE vendas_animais (
    id_venda INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_venda TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    id_animal INT NOT NULL,
    quantidade INT NOT NULL,
    kg_totais DECIMAL(10, 2) NOT NULL,
    media_kg DECIMAL(10,2) NOT NULL,
    preco_kg DECIMAL(10, 2) NOT NULL,
    valor_total decimal(10,2) NOT NULL,
    porce_comissao DECIMAL(10, 2) NOT NULL,
    comissao DECIMAL(10, 2) NOT NULL,
    vendedor VARCHAR(100) NOT NULL,
    comprador VARCHAR(100) NOT NULL,
    pagamento VARCHAR (100) NOT NULL,
    operador VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (id_animal) REFERENCES animais(id) ON DELETE CASCADE
);



CREATE TABLE nascimentos (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	data_nascimento TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	id_animal INT NOT NULL,
    quantidade INT NOT NULL,
    observacao VARCHAR(200),
    local_nasc VARCHAR(100) NOT NULL,
    operador VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animais (id) ON DELETE CASCADE
);

CREATE TABLE falecimentos (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	data_morte TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	id_animal INT NOT NULL,
    quantidade INT NOT NULL,
    observacao VARCHAR(200),
    local_falecimento VARCHAR(100),
    operador VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_animal) REFERENCES animais (id) ON DELETE CASCADE
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
DELETE FROM animais;
Drop table vendas_animais;

select * from usuarios;
select * from animais;
select * from fornecedores;
select * from vendas_animais;
select * from falecimentos;

SELECT id_compra, DATE_FORMAT(data_compra, '%d/%m/%Y %H:%i:%s') as data_formatada, a.descricao as animal_descricao, v.quantidade, media_kg, preco_kg, valor_total, criador, pagador, pagamento, local_compra, operador 
                             FROM compras_animais v 
                            JOIN animais a ON v.id_animal = a.id 
                            WHERE data_compra BETWEEN  '2023/02/01'  AND  '2023/08/31'
                            ORDER BY quantidade DESC;

-- relatorio ja formatado de vendas em um intervalo de tempo
SELECT 
    id_venda, 
    DATE_FORMAT(data_venda, '%d/%m/%Y %H:%i:%s') as data_formatada, 
    a.descricao as animal_descricao,
    v.quantidade,
    media_kg,
     CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as preco_kg_formatado,
    CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as valor_total_formatado,
    vendedor,
    comprador,
    pagamento,
    local_venda,
    operador
FROM vendas_animais v
JOIN animais a ON v.id_animal = a.id
WHERE data_venda BETWEEN '2023-07-20' AND '2023-08-31'
ORDER BY data_venda;

SELECT 
	f.id,
	DATE_FORMAT(data_morte, '%d/%m/%Y') as data_formatada, 
	a.descricao as animal_descricao,
    f.quantidade,
    observacao,
    local_fale,
    operador
    FROM falecimentos f
JOIN animais a ON f.id_animal = a.id
WHERE n.data_morte BETWEEN '2023-07-20' AND '2023-10-31';

SELECT 
    id_compra, 
    DATE_FORMAT(data_compra, '%d/%m/%Y %H:%i:%s') as data_formatada, 
    c.quantidade,
    c.media_kg,
	CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(c.preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as preco_kg_formatado,
    CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(c.valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as valor_total_formatado,
    CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(c.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as porce_formatado,
    CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(c.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as comissao_formatado,
    c.criador,
    c.pagador,
    c.pagamento,
    c.operador
FROM compras_animais c
JOIN animais a ON c.id_animal = a.id
WHERE c.data_compra BETWEEN '2023-07-20' AND '2023-12-31'
ORDER BY c.data_compra;


-- lsita de animais mais vendidos
SELECT a.descricao AS nome_animal, SUM(va.quantidade) AS total_vendido
FROM vendas_animais va 
JOIN animais a ON va.id_animal = a.id
GROUP BY va.id_animal, a.descricao
ORDER BY total_vendido DESC;

-- lista de vendas entre determinadas datas
SELECT *
FROM vendas_animais
WHERE data_venda >= '2023-10-13' AND data_venda <= '2023-12-31';

SELECT *
FROM compras_animais
WHERE data_compra >= '2023-10-13' AND data_compra <= '2023-12-31';

SELECT * FROM nascimentos;

describe usuarios;
describe animais;
describe compras_animais;
describe vendas_animais;
describe nascimentos;
describe falecimentos;



-- }






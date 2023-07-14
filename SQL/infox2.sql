use dbinfox;

CREATE TABLE usuarios (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome varchar(30) not null,
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
    idade ENUM('terneiro', 'novilho', 'vaca_velha') NOT NULL,
    sexo ENUM('boi', 'touro_reprodutor', 'femea') NOT NULL,
    raca VARCHAR(100) NOT NULL
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
    operador VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (id_animal) REFERENCES animais(id)
);


CREATE TABLE vendas_animais (
    id_venda INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_venda TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    id_animal INT NOT NULL,
    media_kg DECIMAL(10,2) NOT NULL,
    preco_kg DECIMAL(10, 2) NOT NULL,
    valor_total decimal(10,2) NOT NULL,
    comprador VARCHAR(100) NOT NULL,
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

-- alterar quantidade venda UPDATE animais SET quantidade = quantidade - ? WHERE id_animal = ?

-- CONSULTAS

select * from usuarios;
select * from fornecedores;
describe usuarios;




CREATE TABLE `animais` (
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  descricao varchar(100) DEFAULT NULL,
  raca varchar(100) NOT NULL,
  idade enum('terneiro','novilho','vaca_velha') NOT NULL,
  sexo enum('boi','touro_reprodutor','femea') NOT NULL,
  numero_fornecedor int NOT NULL,
 
 FOREIGN KEY (`numero_fornecedor`) REFERENCES `fornecedores` (`id`) ON DELETE CASCADE
);

INSERT INTO `animais` VALUES 
  (1,'Vaca preta','Holandesa','vaca_velha','femea',1),
  (2,'Touro','Holandesa','novilho','touro_reprodutor',1);

CREATE TABLE `compra` (
  `id_compra` int NOT NULL AUTO_INCREMENT,
  `data_compra` date NOT NULL,
  `valor_total` decimal(10,2) NOT NULL,
  `id_fornecedor` int NOT NULL,
  PRIMARY KEY (`id_compra`),

  FOREIGN KEY (`id_fornecedor`) REFERENCES `fornecedores` (`id`) ON DELETE CASCADE
);

INSERT INTO `compra` VALUES 
  (1,'2022-03-23',5000.00,1),
  (2,'2022-01-01',10000.00,1),
  (3,'2022-01-01',10000.00,1),
  (4,'2022-01-01',10000.00,1),
  (5,'2022-01-01',10000.00,1);

CREATE TABLE `despesas` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `descricao` varchar(100) NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `data_despesa` date NOT NULL
);

CREATE TABLE `fornecedores` (
  `id` int  PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nome` varchar(50)  UNIQUE KEY NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL
);

INSERT INTO `fornecedores` VALUES 
  (1,'Fornecedor 1','11 1111-1111','fornecedor1@gmail.com','Rua A, 123'),
  (2,'Fornecedor 2','22 2222-2222','fornecedor2@gmail.com','Rua B, 456');

CREATE TABLE lote (
id_lote int PRIMARY KEY NOT NULL AUTO_INCREMENT,
data_transacao date NOT NULL,
tipo_transacao enum('compra','venda') NOT NULL,
quantidade int NOT NULL,
valor_unitario decimal(10,2) NOT NULL,
id_animal int NOT NULL,
id_compra int NOT NULL,
id_venda int DEFAULT NULL,
FOREIGN KEY (id_animal) REFERENCES animais (id) ON DELETE CASCADE,
FOREIGN KEY (id_compra) REFERENCES compra (id_compra) ON DELETE CASCADE,
FOREIGN KEY (id_venda) REFERENCES venda (id_venda) ON DELETE CASCADE
);

CREATE TABLE lucros (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
descricao varchar(100) NOT NULL,
valor decimal(10,2) NOT NULL,
data_lucro date NOT NULL
);

CREATE TABLE usuarios (
id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
login varchar(15) UNIQUE KEY NOT NULL,
senha varchar(15) NOT NULL,
tipo enum('admin','consulta') NOT NULL DEFAULT 'consulta'
);

CREATE TABLE venda (
id_venda int PRIMARY KEY NOT NULL AUTO_INCREMENT,
data_venda date NOT NULL,
valor_total decimal(10,2) NOT NULL
);

INSERT INTO lote VALUES (3,'2022-01-01','compra',50,200.00,1,1,NULL);
INSERT INTO venda VALUES (1,'2022-01-10',15000.00);



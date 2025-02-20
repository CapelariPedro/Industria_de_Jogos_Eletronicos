CREATE DATABASE estoque_db;
USE estoque_db;

CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    localizacao VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL
);

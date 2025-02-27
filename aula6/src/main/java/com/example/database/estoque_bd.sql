CREATE DATABASE estoque_db;
USE estoque_db;

CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    localizacao VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL
);

CREATE TABLE funcionarios(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(30) NOT NULL,
    cargo VARCHAR(100) NOT NULL,
    salario VARCHAR(100) NOT NULL,
    setor VARCHAR(100) NOT NULL
);

CREATE DATABASE industria_automotiva;
USE industria_automotiva;

-- Tabela Funcionários (RH)
CREATE TABLE funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cargo VARCHAR(50),
    salario DECIMAL(10,2),
    data_contratacao DATE
);

-- Tabela Produção
CREATE TABLE producao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255),
    data_inicio DATE,
    data_fim DATE,
    status ENUM('Em andamento', 'Concluído', 'Cancelado')
);

-- Tabela Estoque
CREATE TABLE estoque (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_peca VARCHAR(100),
    quantidade INT,
    fornecedor VARCHAR(100),
    data_entrada DATE
);

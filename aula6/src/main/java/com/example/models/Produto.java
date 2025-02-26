package com.example.models;

public class Produto {
    private int id;
    private String nome;
    private int quantidade;
    private double preco;
    private String localizacao;
    private String categoria;

    public Produto(int id, String nome, int quantidade, double preco, String localizacao, String categoria) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.localizacao = localizacao;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public String getCategoria() {
        return categoria;
    }
}
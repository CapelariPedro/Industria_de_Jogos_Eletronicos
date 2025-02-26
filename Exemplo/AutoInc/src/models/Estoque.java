package models;

public class Estoque {
    private int id;
    private String nomePeca;
    private int quantidade;
    private String fornecedor;

    public Estoque(int id, String nomePeca, int quantidade, String fornecedor) {
        this.id = id;
        this.nomePeca = nomePeca;
        this.quantidade = quantidade;
        this.fornecedor = fornecedor;
    }

    public int getId() { return id; }
    public String getNomePeca() { return nomePeca; }
    public int getQuantidade() { return quantidade; }
    public String getFornecedor() { return fornecedor; }
}

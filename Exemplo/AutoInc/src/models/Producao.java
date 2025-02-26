package models;

public class Producao {
    private int id;
    private String descricao;
    private String status;

    public Producao(int id, String descricao, String status) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public String getStatus() { return status; }
}

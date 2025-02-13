package model;

import javafx.beans.property.*;

public class Funcionario {
    private IntegerProperty id;
    private StringProperty nome;
    private StringProperty cargo;
    private DoubleProperty salario;

    public Funcionario(int id, String nome, String cargo, double salario) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.cargo = new SimpleStringProperty(cargo);
        this.salario = new SimpleDoubleProperty(salario);
    }

    public Funcionario() {
        this(0, "", "", 0.0);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public String getCargo() { return cargo.get(); }
    public void setCargo(String cargo) { this.cargo.set(cargo); }
    public StringProperty cargoProperty() { return cargo; }

    public double getSalario() { return salario.get(); }
    public void setSalario(double salario) { this.salario.set(salario); }
    public DoubleProperty salarioProperty() { return salario; }
}
package com.example.models;

public class Funcionario {
    private int id;
    private String nome;
    private String matricula;
    private String cargo;
    private double salario;
    private String setor;

    public Funcionario(int id, String nome,String matricula, String cargo, double salario, String setor) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.cargo = cargo;
        this.salario = salario;
        this.setor = setor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}

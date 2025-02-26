package controllers;

import application.Database;
import models.Funcionario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class FuncionarioController {
    @FXML private TextField txtNome;
    @FXML private TextField txtCargo;
    @FXML private TextField txtSalario;
    @FXML private TableView<Funcionario> tabelaFuncionarios;
    private ObservableList<Funcionario> listaFuncionarios = FXCollections.observableArrayList();

    public void initialize() {
        carregarFuncionarios();
    }

    public void adicionarFuncionario() {
        String nome = txtNome.getText();
        String cargo = txtCargo.getText();
        double salario = Double.parseDouble(txtSalario.getText());

        String sql = "INSERT INTO funcionarios (nome, cargo, salario) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cargo);
            stmt.setDouble(3, salario);
            stmt.executeUpdate();
            carregarFuncionarios();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarFuncionarios() {
        listaFuncionarios.clear();
        String sql = "SELECT * FROM funcionarios";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                listaFuncionarios.add(new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cargo"),
                        rs.getDouble("salario")
                ));
            }
            tabelaFuncionarios.setItems(listaFuncionarios);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

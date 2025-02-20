package com.example.controllers;

import com.example.database.Database;
import com.example.models.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ProdutoController {
    @FXML private TextField txtNome;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtPreco;
    @FXML private TableView<Produto> tableProdutos;
    @FXML private TableColumn<Produto, Integer> colId;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, Integer> colQuantidade;
    @FXML private TableColumn<Produto, Double> colPreco;
    @FXML private TableColumn<Produto, String> colLocalizacao;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtLocalizacao;

    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    @FXML
    private void salvarProduto() {
        String nome = txtNome.getText();
        int quantidade = Integer.parseInt(txtQuantidade.getText());
        double preco = Double.parseDouble(txtPreco.getText());

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO produtos (nome, quantidade, preco, localizacao, categoria) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, nome);
            stmt.setInt(2, quantidade);
            stmt.setDouble(3, preco);
            stmt.setString(4, txtLocalizacao.getText());
            stmt.setString(5, cmbCategoria.getValue());
            stmt.executeUpdate();

            carregarProdutos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colLocalizacao.setCellValueFactory(new PropertyValueFactory<>("localizacao"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        cmbCategoria.getItems().addAll("Eletrônicos", "Maquinas", "Peças", "Utilitários");

        carregarProdutos();
    }

    private void carregarProdutos() {
        listaProdutos.clear();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produtos")) {

            while (rs.next()) {
                listaProdutos.add(new Produto(rs.getInt("id"), rs.getString("nome"), rs.getInt("quantidade"), rs.getDouble("preco"), rs.getString("localizacao"), rs.getString("categoria")));
            }
            tableProdutos.setItems(listaProdutos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

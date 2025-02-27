package com.example.controllers;

import com.example.database.Database;
import com.example.models.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.input.MouseEvent;

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

    // Campos para atualização
    @FXML private TextField txtIdAtualizar;
    @FXML private TextField txtNomeAtualizar;
    @FXML private TextField txtQuantidadeAtualizar;
    @FXML private TextField txtPrecoAtualizar;
    @FXML private ComboBox<String> cmbCategoriaAtualizar;
    @FXML private TextField txtLocalizacaoAtualizar;

    //import dos filtros
    @FXML private TextField filtroNome;
    @FXML private TextField filtroQuantidade;
    @FXML private TextField filtroPreco;
    @FXML private TextField filtroLocalizacao;
    @FXML private ComboBox<String> filtroCategoria;
    @FXML private Button btnLimparFiltro;

    //Controle de tabs
    @FXML private TabPane tabPane;
    @FXML private Tab tabAtualizar;
    @FXML private Tab tabListaProdutos;

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
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Produto salvo com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar o produto!" + e.getMessage());
        }
    }

    @FXML
    private void atualizarProduto() {
        int id = Integer.parseInt(txtIdAtualizar.getText());
        String nome = txtNomeAtualizar.getText();
        int quantidade = Integer.parseInt(txtQuantidadeAtualizar.getText());
        double preco = Double.parseDouble(txtPrecoAtualizar.getText());

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE produtos SET nome = ?, quantidade = ?, preco = ?, localizacao = ?, categoria = ? WHERE id = ?")) {

            stmt.setString(1, nome);
            stmt.setInt(2, quantidade);
            stmt.setDouble(3, preco);
            stmt.setString(4, txtLocalizacaoAtualizar.getText());
            stmt.setString(5, cmbCategoriaAtualizar.getValue());
            stmt.setInt(6, id);
            stmt.executeUpdate();

            carregarProdutos();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Produto atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar o produto!" + e.getMessage());
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

        cmbCategoria.getItems().addAll("Eletrônicos", "Máquinas", "Peças", "Utilitários","Acessórios");
        cmbCategoriaAtualizar.getItems().addAll("Eletrônicos", "Máquinas", "Peças", "Utilitários","Acessórios");
        filtroCategoria.getItems().addAll("Eletrônicos", "Máquinas", "Peças", "Utilitários","Acessórios");

        carregarProdutos();

        // Adiciona um listener para a seleção de itens na tabela
        tableProdutos.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                preencherCamposAtualizacao();
            }
        });
    }

    private void preencherCamposAtualizacao() {
        Produto produtoSelecionado = tableProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            txtIdAtualizar.setText(String.valueOf(produtoSelecionado.getId()));
            txtNomeAtualizar.setText(produtoSelecionado.getNome());
            txtQuantidadeAtualizar.setText(String.valueOf(produtoSelecionado.getQuantidade()));
            txtPrecoAtualizar.setText(String.valueOf(produtoSelecionado.getPreco()));
            txtLocalizacaoAtualizar.setText(produtoSelecionado.getLocalizacao());
            cmbCategoriaAtualizar.setValue(produtoSelecionado.getCategoria());

            // Muda para a aba de atualização
            tabPane.getSelectionModel().select(tabAtualizar);
        }
    }

    @FXML
    private void limparCamposAtualizacao(){
        txtIdAtualizar.clear();
        txtNomeAtualizar.clear();
        txtQuantidadeAtualizar.clear();
        txtPrecoAtualizar.clear();
        txtLocalizacaoAtualizar.clear();
        cmbCategoriaAtualizar.setValue(null);

        //volta para a tab de visualização
        tabPane.getSelectionModel().select(tabListaProdutos);
    }

    //função para filtrar os produtos
    @FXML
    private void filtrarProdutos() {
        FilteredList<Produto> dadosFiltrados = new FilteredList<>(listaProdutos, p -> true);

        dadosFiltrados.setPredicate(produto -> {
            if (!filtroNome.getText().isEmpty() && !produto.getNome().toLowerCase().contains(filtroNome.getText().toLowerCase())) {
                return false;
            }
            if (!filtroQuantidade.getText().isEmpty() && !String.valueOf(produto.getQuantidade()).contains(filtroQuantidade.getText())) {
                return false;
            }
            if (!filtroPreco.getText().isEmpty() && !String.valueOf(produto.getPreco()).contains(filtroPreco.getText())) {
                return false;
            }
            if (!filtroLocalizacao.getText().isEmpty() && !produto.getLocalizacao().toLowerCase().contains(filtroLocalizacao.getText().toLowerCase())) {
                return false;
            }
            if (filtroCategoria.getValue() != null && !filtroCategoria.getValue().isEmpty() && !produto.getCategoria().toLowerCase().contains(filtroCategoria.getValue().toLowerCase())) {
                return false;
            }
            return true;
        });

        tableProdutos.setItems(dadosFiltrados);
    }

    @FXML
    private void limparFiltro() {
        filtroNome.clear();
        filtroQuantidade.clear();
        filtroPreco.clear();
        filtroLocalizacao.clear();
        filtroCategoria.setValue(null);
        tableProdutos.setItems(listaProdutos);
    }

    @FXML
    private void excluirProduto(){
        Produto produtoSelecionado = tableProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM produtos WHERE id = ?")) {

                stmt.setInt(1, produtoSelecionado.getId());
                stmt.executeUpdate();

                carregarProdutos();

                mostrarAlerta(Alert.AlertType.INFORMATION,"Sucesso" , "Produto excluído com sucesso!");

                //volta para a tab de visualização
                tabPane.getSelectionModel().select(tabListaProdutos);

            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir o produto!" + e.getMessage());
            }
        }
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
            mostrarAlerta(Alert.AlertType.ERROR,"Erro","Erro ao carregar Produtos: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
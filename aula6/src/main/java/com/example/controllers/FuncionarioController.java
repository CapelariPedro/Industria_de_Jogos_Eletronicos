package com.example.controllers;

import com.example.database.Database;
import com.example.models.Funcionario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class FuncionarioController {
    @FXML private TextField txtNomeFunc;
    @FXML private TextField txtMatriculaFunc;
    @FXML private ComboBox<String> cmbCargoFunc;
    @FXML private TextField txtSalarioFunc;
    @FXML private ComboBox<String> cmbSetorFunc;

    @FXML private TextField txtIdAtualizarFunc;
    @FXML private TextField txtNomeAtualizarFunc;
    @FXML private TextField txtMatriculaAtualizarFunc;
    @FXML private TextField txtSalarioAtualizarFunc;
    @FXML private ComboBox<String> cmbCargoAtualizarFunc;
    @FXML private ComboBox<String> cmbSetorAtualizarFunc;

    @FXML private TableView<Funcionario> tableFuncionarios;
    @FXML private TableColumn<Funcionario, Integer> colIdFunc;
    @FXML private TableColumn<Funcionario, String> colNomeFunc;
    @FXML private TableColumn<Funcionario, String> colMatriculaFunc;
    @FXML private TableColumn<Funcionario, String> colCargoFunc;
    @FXML private TableColumn<Funcionario, Double> colSalarioFunc;
    @FXML private TableColumn<Funcionario, String> colSetorFunc;

    @FXML private TextField filtroNomeFunc;
    @FXML private TextField filtroMatriculaFunc;
    @FXML private TextField filtroCargoFunc;
    @FXML private TextField filtroSetorFunc;
    @FXML private TextField filtroSalarioFunc;

    @FXML private TabPane tabPaneFuncionario;
    @FXML private Tab tabCadastrarFuncionario;
    @FXML private Tab tabAtualizarFuncionario;
    @FXML private Tab tabListarFuncionario;

    private ObservableList<Funcionario> listaFuncionarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIdFunc.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeFunc.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatriculaFunc.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colCargoFunc.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colSalarioFunc.setCellValueFactory(new PropertyValueFactory<>("salario"));
        colSetorFunc.setCellValueFactory(new PropertyValueFactory<>("setor"));

        cmbCargoFunc.getItems().addAll("Gerente", "Operador", "TI", "Assistente");
        cmbSetorFunc.getItems().addAll("Produção", "Estoque", "Financeiro", "RH");
        cmbCargoAtualizarFunc.getItems().addAll("Gerente", "Operador", "TI", "Assistente");
        cmbSetorAtualizarFunc.getItems().addAll("Produção", "Estoque", "Financeiro", "RH");

        carregarFuncionarios();

        tableFuncionarios.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                preencherCamposAtualizacao();
            }
        });
    }

    @FXML
    public void salvarFuncionario() {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO funcionarios (nome, matricula, cargo, salario, setor) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, txtNomeFunc.getText());
            stmt.setString(2, txtMatriculaFunc.getText());
            stmt.setString(3, cmbCargoFunc.getValue());
            stmt.setDouble(4, Double.parseDouble(txtSalarioFunc.getText()));
            stmt.setString(5, cmbSetorFunc.getValue());
            stmt.executeUpdate();
            carregarFuncionarios();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário salvo com sucesso!");
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar funcionário: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarFuncionario() {
        int id = Integer.parseInt(txtIdAtualizarFunc.getText());
        String nome = txtNomeAtualizarFunc.getText();
        String matricula = txtMatriculaAtualizarFunc.getText();
        String cargo = cmbCargoAtualizarFunc.getValue();
        double salario = Double.parseDouble(txtSalarioAtualizarFunc.getText());
        String setor = cmbSetorAtualizarFunc.getValue();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE funcionarios SET nome = ?, matricula = ?, cargo = ?, salario = ?, setor = ? WHERE id = ?")) {
            stmt.setString(1, nome);
            stmt.setString(2, matricula);
            stmt.setString(3, cargo);
            stmt.setDouble(4, salario);
            stmt.setString(5, setor);
            stmt.setInt(6, id);
            stmt.executeUpdate();
            carregarFuncionarios();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário atualizado com sucesso!");
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar funcionário: " + e.getMessage());
        }
    }

    public void limparCamposAtualizacao(){
        txtIdAtualizarFunc.clear();
        txtNomeAtualizarFunc.clear();
        txtMatriculaAtualizarFunc.clear();
        txtSalarioAtualizarFunc.clear();
        cmbCargoAtualizarFunc.setValue(null);
        cmbSetorAtualizarFunc.setValue(null);

        tabPaneFuncionario.getSelectionModel().select(tabListarFuncionario);
    }

    private void preencherCamposAtualizacao() {
        Funcionario funcionarioSelecionado = tableFuncionarios.getSelectionModel().getSelectedItem();
        if (funcionarioSelecionado != null) {
            txtIdAtualizarFunc.setText(String.valueOf(funcionarioSelecionado.getId()));
            txtNomeAtualizarFunc.setText(funcionarioSelecionado.getNome());
            txtMatriculaAtualizarFunc.setText(funcionarioSelecionado.getMatricula());
            txtSalarioAtualizarFunc.setText(String.valueOf(funcionarioSelecionado.getSalario()));
            cmbCargoAtualizarFunc.setValue(funcionarioSelecionado.getCargo());
            cmbSetorAtualizarFunc.setValue(funcionarioSelecionado.getSetor());

            tabPaneFuncionario.getSelectionModel().select(tabAtualizarFuncionario);
        }
    }

    private void carregarFuncionarios() {
        listaFuncionarios.clear();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM funcionarios")) {

            while (rs.next()) {
                listaFuncionarios.add(new Funcionario(rs.getInt("id"), rs.getString("nome"), rs.getString("matricula"), rs.getString("cargo"), rs.getDouble("salario"), rs.getString("setor")));
            }
            tableFuncionarios.setItems(listaFuncionarios);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    @FXML
    public void filtrarFuncionarios() {
        FilteredList<Funcionario> dadosFiltrados = new FilteredList<>(listaFuncionarios, p -> true);

        dadosFiltrados.setPredicate(funcionario -> {
            if (!filtroNomeFunc.getText().isEmpty() && !funcionario.getNome().toLowerCase().contains(filtroNomeFunc.getText().toLowerCase())) {
                return false;
            }
            if (!filtroMatriculaFunc.getText().isEmpty() && !funcionario.getMatricula().toLowerCase().contains(filtroMatriculaFunc.getText().toLowerCase())) {
                return false;
            }
            if (!filtroCargoFunc.getText().isEmpty() && !funcionario.getCargo().toLowerCase().contains(filtroCargoFunc.getText().toLowerCase())) {
                return false;
            }
            if (!filtroSetorFunc.getText().isEmpty() && !funcionario.getSetor().toLowerCase().contains(filtroSetorFunc.getText().toLowerCase())) {
                return false;
            }
            if (!filtroSalarioFunc.getText().isEmpty() && !String.valueOf(funcionario.getSalario()).toLowerCase().contains(filtroSalarioFunc.getText().toLowerCase())) {
                return false;
            }
            return true;
        });

        tableFuncionarios.setItems(dadosFiltrados);
    }

    @FXML
    public void limparFiltro() {
        filtroNomeFunc.clear();
        filtroMatriculaFunc.clear();
        filtroCargoFunc.clear();
        filtroSetorFunc.clear();
        filtroSalarioFunc.clear();
        tableFuncionarios.setItems(listaFuncionarios);
    }

    @FXML
    public void excluirFuncionario(){
        Funcionario funcionarioSelecionado = tableFuncionarios.getSelectionModel().getSelectedItem();
        if (funcionarioSelecionado != null) {
            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM funcionarios WHERE id = ?")) {
                stmt.setInt(1, funcionarioSelecionado.getId());
                stmt.executeUpdate();
                carregarFuncionarios();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário excluído com sucesso!");
                tabPaneFuncionario.getSelectionModel().select(tabListarFuncionario);
            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao excluir funcionário: " + e.getMessage());
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um funcionário para excluir!");
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
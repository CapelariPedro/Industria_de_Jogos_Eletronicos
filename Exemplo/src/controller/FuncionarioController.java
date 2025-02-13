package controller;

import dao.FuncionarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import model.Funcionario;

public class FuncionarioController {
    @FXML private TextField txtNome, txtCargo, txtSalario;
    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, String> colNome, colCargo;
    @FXML private TableColumn<Funcionario, Integer> colId;
    @FXML private TableColumn<Funcionario, Double> colSalario;

    private FuncionarioDAO dao = new FuncionarioDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCargo.setCellValueFactory(cellData -> cellData.getValue().cargoProperty());
        colSalario.setCellValueFactory(cellData -> cellData.getValue().salarioProperty().asObject());
        atualizarTabela();
    }

    @FXML
    public void adicionarFuncionario() {
        Funcionario funcionario = new Funcionario(0, txtNome.getText(), txtCargo.getText(), Double.parseDouble(txtSalario.getText()));
        dao.adicionarFuncionario(funcionario);
        atualizarTabela();
    }

    public void atualizarTabela() {
        ObservableList<Funcionario> lista = FXCollections.observableArrayList(dao.listarFuncionarios());
        tabelaFuncionarios.setItems(lista);
    }
}
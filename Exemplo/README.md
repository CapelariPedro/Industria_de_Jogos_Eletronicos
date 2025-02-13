Projeto de Gestão de Funcionários
Este projeto é uma aplicação JavaFX que permite gerenciar funcionários, incluindo operações de adicionar, listar, atualizar e deletar funcionários. Ele utiliza JavaFX para a interface gráfica e JDBC para a conexão com um banco de dados MySQL.

Estrutura do Projeto
O projeto é organizado da seguinte forma:

Main.java: Classe principal que inicia a aplicação JavaFX.

FuncionarioView.fxml: Arquivo FXML que define a interface gráfica da aplicação.

DatabaseConnection.java: Classe utilitária para gerenciar a conexão com o banco de dados MySQL.

Funcionario.java: Classe modelo que representa um funcionário.

FuncionarioDAO.java: Classe de acesso a dados (DAO) que gerencia as operações de CRUD no banco de dados.

FuncionarioController.java: Controlador que gerencia a interação entre a interface gráfica e o banco de dados.

Explicação do Código
1. Main.java
Esta é a classe principal que inicia a aplicação JavaFX. Ela carrega o arquivo FXML (FuncionarioView.fxml) e exibe a interface gráfica.


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega o arquivo FXML que define a interface gráfica
        Parent root = FXMLLoader.load(getClass().getResource("/view/FuncionarioView.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Gestão de Funcionários");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Inicia a aplicação JavaFX
    }
}


2. FuncionarioView.fxml
Este arquivo define a interface gráfica da aplicação utilizando FXML. Ele contém campos de texto para inserir o nome, cargo e salário do funcionário, um botão para adicionar o funcionário e uma tabela para listar os funcionários.


<VBox alignment="CENTER" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="400.0" prefWidth="900.0" spacing="10" xmlns="http://javafx.com/javafx/23.0.1" xmlns:fx="http://javafx.com/fxml/1" fx:controller="controller.FuncionarioController">
    <Label text="Gestão de Funcionários" />
    <TextField fx:id="txtNome" prefHeight="26.0" prefWidth="659.0" promptText="Nome" />
    <TextField fx:id="txtCargo" promptText="Cargo" />
    <TextField fx:id="txtSalario" promptText="Salário" />
    <Button onAction="#adicionarFuncionario" text="Adicionar" />
    <TableView fx:id="tabelaFuncionarios">
        <columns>
            <TableColumn fx:id="colId" text="ID" />
            <TableColumn fx:id="colNome" text="Nome" />
            <TableColumn fx:id="colCargo" text="Cargo" />
            <TableColumn fx:id="colSalario" text="Salário" />
        </columns>
    </TableView>
</VBox>


3. DatabaseConnection.java
Esta classe gerencia a conexão com o banco de dados MySQL. Ela utiliza o JDBC para estabelecer a conexão e retorna um objeto Connection que pode ser usado para executar consultas SQL.


package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/funcionarios_db";
    private static final String USER = "root"; // Alterar conforme seu usuário MySQL
    private static final String PASSWORD = "root"; // Alterar conforme sua senha

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Conexão bem-sucedida!");
            return conn;
        } catch (SQLException e) {
            System.out.println(" Erro ao conectar ao banco de dados:");
            e.printStackTrace();  // Exibe erro detalhado no console
            return null;
        }
    }
}


4. Funcionario.java
Esta classe representa o modelo de dados de um funcionário. Ela utiliza propriedades do JavaFX (IntegerProperty, StringProperty, DoubleProperty) para facilitar a vinculação de dados com a interface gráfica.


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


5. FuncionarioDAO.java
Esta classe é responsável por realizar as operações de CRUD (Create, Read, Update, Delete) no banco de dados. Ela utiliza a classe DatabaseConnection para obter uma conexão com o banco de dados.

package dao;

import model.Funcionario;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void adicionarFuncionario(Funcionario funcionario) {
        String sql = "INSERT INTO funcionarios (nome, cargo, salario) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setDouble(3, funcionario.getSalario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Funcionario> listarFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                funcionarios.add(new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cargo"),
                        rs.getDouble("salario")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }

    public void atualizarFuncionario(Funcionario funcionario) {
        String sql = "UPDATE funcionarios SET nome = ?, cargo = ?, salario = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setDouble(3, funcionario.getSalario());
            stmt.setInt(4, funcionario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarFuncionario(int id) {
        String sql = "DELETE FROM funcionarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


6. FuncionarioController.java
Esta classe é o controlador da aplicação. Ela gerencia a interação entre a interface gráfica (definida em FuncionarioView.fxml) e o banco de dados (através da classe FuncionarioDAO).


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
        // Vincula as colunas da tabela às propriedades do modelo Funcionario
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCargo.setCellValueFactory(cellData -> cellData.getValue().cargoProperty());
        colSalario.setCellValueFactory(cellData -> cellData.getValue().salarioProperty().asObject());
        atualizarTabela(); // Atualiza a tabela com os dados do banco de dados
    }

    @FXML
    public void adicionarFuncionario() {
        // Cria um novo funcionário com os dados inseridos pelo usuário
        Funcionario funcionario = new Funcionario(0, txtNome.getText(), txtCargo.getText(), Double.parseDouble(txtSalario.getText()));
        dao.adicionarFuncionario(funcionario); // Adiciona o funcionário ao banco de dados
        atualizarTabela(); // Atualiza a tabela após a adição
    }

    public void atualizarTabela() {
        // Obtém a lista de funcionários do banco de dados e atualiza a tabela
        ObservableList<Funcionario> lista = FXCollections.observableArrayList(dao.listarFuncionarios());
        tabelaFuncionarios.setItems(lista);
    }
}


Sequência Lógica do Projeto
Inicialização da Aplicação:

A classe Main inicia a aplicação JavaFX e carrega a interface gráfica definida em FuncionarioView.fxml.

Carregamento da Interface Gráfica:

O arquivo FuncionarioView.fxml define a interface gráfica, que inclui campos de texto para inserir dados do funcionário, um botão para adicionar o funcionário e uma tabela para listar os funcionários.

Conexão com o Banco de Dados:

A classe DatabaseConnection gerencia a conexão com o banco de dados MySQL.

Operações de CRUD:

A classe FuncionarioDAO realiza as operações de CRUD no banco de dados, como adicionar, listar, atualizar e deletar funcionários.

Interação com a Interface Gráfica:

A classe FuncionarioController gerencia a interação entre a interface gráfica e o banco de dados. Quando o usuário clica no botão "Adicionar", o controlador cria um novo funcionário com os dados inseridos e o adiciona ao banco de dados.

Atualização da Tabela:

Após cada operação de CRUD, a tabela é atualizada para refletir as mudanças no banco de dados.

Como Executar o Projeto
Configuração do Banco de Dados:

Certifique-se de que o MySQL está instalado e que o banco de dados funcionarios_db e a tabela funcionarios foram criados.

Execução no VS Code:

Abra o projeto no VS Code e execute a classe Main para iniciar a aplicação.

Teste a Aplicação:

Insira dados de funcionários e verifique se eles são corretamente adicionados, listados, atualizados e deletados.
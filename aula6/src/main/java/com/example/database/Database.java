package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/estoque_db";
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

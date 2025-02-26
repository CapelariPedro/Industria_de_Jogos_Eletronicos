package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/industria_automotiva";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }
}

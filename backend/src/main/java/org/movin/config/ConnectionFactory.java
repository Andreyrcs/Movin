package org.movin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/movin";
    private static final String USUARIO = "root";
    private static final String SENHA = "SUA_SENHA";

    public static Connection getConnection() {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conexao;
        } catch (SQLException erro) {
            System.out.println("Erro ao conectar com o banco.");
            System.out.println(erro.getMessage());
            return null;
        }
    }
}
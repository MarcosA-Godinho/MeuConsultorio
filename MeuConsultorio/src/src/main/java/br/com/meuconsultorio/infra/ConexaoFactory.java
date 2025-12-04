package br.com.meuconsultorio.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFactory {

    private static final String URL = "jdbc:sqlite:consultorio.db";

    public static Connection getConnection() {
        try {
            //Vai tentar criar a conexão
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            // Se der erro na conexão, lança uma exceção de tempo de execução (Unchecked)
            // Isso evita que tenhamos que ficar colocando try-catch em todo lugar
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
    }
}

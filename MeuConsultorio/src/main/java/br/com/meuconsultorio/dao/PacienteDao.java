package br.com.meuconsultorio.dao;

import br.com.meuconsultorio.infra.ConexaoFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PacienteDao {

    //METODO PARA CRIAR A TABELA NO BANCO (SÓ RODA UMA VEZ)
    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXIST paciente (
                id INTERGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                cpf TEXT,
                data_nascimento DATE,
                telefone TEXT,
                endereco TEXT,
                historico_geral TEXT
                );
                """;
        try (Connection conn = ConexaoFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabela 'paciente' verificada/criada com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage());
        }
    }
}

package br.com.meuconsultorio.dao;

import br.com.meuconsultorio.infra.ConexaoFactory;
import br.com.meuconsultorio.model.Sessao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SessaoDao {

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS sessao (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_paciente INTEGER,
                    data TEXT,
                    hora TEXT,
                    status TEXT,
                    observacao TEXT,
                    FOREIGN KEY(id_paciente) REFERENCES paciente(id)
                );
                """;

        try (Connection conn = ConexaoFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'sessao' verificada/criada!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela sessao: " + e.getMessage());
        }
    }

    public void agendar(Sessao s) {
        String sql = "INSERT INTO sessao (id_paciente, data, hora, status, observacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, s.getIdPaciente());
            ps.setString(2, s.getData());
            ps.setString(3, s.getHora());
            ps.setString(4, "Agendado"); // Padrão ao criar
            ps.setString(5, ""); // Começa sem observação

            ps.execute();
            System.out.println("Sessão agendada com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao agendar: " + e.getMessage());
        }
    }

    // LISTAR SESSÕES (Trazendo o nome do paciente junto!)
    public List<Sessao> listarPorData(String dataFiltro) {
        // O Pulo do Gato: JOIN
        String sql = """
            SELECT s.*, p.nome as nome_paciente 
            FROM sessao s
            INNER JOIN paciente p ON s.id_paciente = p.id
            WHERE s.data = ?
            ORDER BY s.hora ASC
        """;

        List<Sessao> lista = new ArrayList<>();

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dataFiltro);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sessao s = new Sessao();
                s.setId(rs.getLong("id"));
                s.setIdPaciente(rs.getLong("id_paciente"));
                s.setData(rs.getString("data"));
                s.setHora(rs.getString("hora"));
                s.setStatus(rs.getString("status"));
                s.setObservacao(rs.getString("observacao"));

                // Preenchemos o nome que veio do JOIN
                s.setNomePaciente(rs.getString("nome_paciente"));

                lista.add(s);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar agenda: " + e.getMessage());
        }
        return lista;
    }
}
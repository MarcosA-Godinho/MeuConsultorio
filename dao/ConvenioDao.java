package br.com.meuconsultorio.dao;

import br.com.meuconsultorio.infra.ConexaoFactory;
import br.com.meuconsultorio.model.Convenio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConvenioDao {

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS convenio (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    valor REAL
                );
                """;
        try (Connection conn = ConexaoFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            // Garante que o "Particular" existe
            if (listarTodos().isEmpty()) {
                cadastrar(new Convenio("Particular", 150.0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cadastrar(Convenio c) {
        String sql = "INSERT INTO convenio (nome, valor) VALUES (?, ?)";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setDouble(2, c.getValor());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Convenio> listarTodos() {
        String sql = "SELECT * FROM convenio";
        List<Convenio> lista = new ArrayList<>();
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Convenio c = new Convenio();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                c.setValor(rs.getDouble("valor"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}
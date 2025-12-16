package br.com.meuconsultorio.dao;

import br.com.meuconsultorio.infra.ConexaoFactory;
import br.com.meuconsultorio.model.Sessao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessaoDao {

    public void criarTabela() {
        // Agora tem a coluna id_convenio
        String sql = """
                CREATE TABLE IF NOT EXISTS sessao (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_paciente INTEGER,
                    id_convenio INTEGER, 
                    data TEXT,
                    hora TEXT,
                    status TEXT,
                    observacao TEXT,
                    FOREIGN KEY(id_paciente) REFERENCES paciente(id),
                    FOREIGN KEY(id_convenio) REFERENCES convenio(id)
                );
                """;
        try (Connection conn = ConexaoFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void agendar(Sessao s) {
        // Incluimos o id_convenio no INSERT
        String sql = "INSERT INTO sessao (id_paciente, id_convenio, data, hora, status, observacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, s.getIdPaciente());
            // Se não tiver convênio selecionado, salva como 0 ou null (mas vamos forçar ter um)
            ps.setLong(2, s.getIdConvenio());
            ps.setString(3, s.getData());
            ps.setString(4, s.getHora());
            ps.setString(5, "Agendado");
            ps.setString(6, "");

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao agendar: " + e.getMessage());
        }
    }

    public void atualizarStatus(Long idSessao, String novoStatus) {
        String sql = "UPDATE sessao SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoStatus);
            ps.setLong(2, idSessao);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // O SUPER SELECT (DASHBOARD)
    public List<Sessao> listarPorFiltro(String statusFiltro) {
        // JOIN DUPLO: Traz nome do Paciente E nome do Convênio
        String baseSql = """
            SELECT s.*, p.nome as nome_paciente, c.nome as nome_convenio
            FROM sessao s
            INNER JOIN paciente p ON s.id_paciente = p.id
            LEFT JOIN convenio c ON s.id_convenio = c.id
        """;

        String sql;
        if (statusFiltro.equals("Todos")) {
            sql = baseSql + " ORDER BY substr(s.data, 7, 4) || substr(s.data, 4, 2) || substr(s.data, 1, 2), s.hora LIMIT 50";
        } else {
            sql = baseSql + " WHERE s.status = ? ORDER BY substr(s.data, 7, 4) || substr(s.data, 4, 2) || substr(s.data, 1, 2), s.hora LIMIT 50";
        }

        List<Sessao> lista = new ArrayList<>();
        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!statusFiltro.equals("Todos")) {
                ps.setString(1, statusFiltro);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sessao s = new Sessao();
                s.setId(rs.getLong("id"));
                s.setIdPaciente(rs.getLong("id_paciente"));
                s.setIdConvenio(rs.getLong("id_convenio")); // Guarda o ID
                s.setNomePaciente(rs.getString("nome_paciente"));
                s.setNomeConvenio(rs.getString("nome_convenio")); // Guarda o Nome (Unimed, etc)
                s.setData(rs.getString("data"));
                s.setHora(rs.getString("hora"));
                s.setStatus(rs.getString("status"));
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Precisamos atualizar o listarPorData (Agenda Diária) também, se quiser ver o convênio lá depois
    public List<Sessao> listarPorData(String data) {
        String sql = """
            SELECT s.*, p.nome as nome_paciente, c.nome as nome_convenio
            FROM sessao s
            INNER JOIN paciente p ON s.id_paciente = p.id
            LEFT JOIN convenio c ON s.id_convenio = c.id
            WHERE s.data = ?
            ORDER BY s.hora ASC
        """;
        // ... (O resto é igual ao método acima: preencher o objeto Sessao) ...
        // Vou resumir para não ficar gigante, mas a lógica é a mesma do listarPorFiltro
        List<Sessao> lista = new ArrayList<>();
        try (Connection conn = ConexaoFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, data);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Sessao s = new Sessao();
                s.setId(rs.getLong("id"));
                s.setIdPaciente(rs.getLong("id_paciente"));
                s.setNomePaciente(rs.getString("nome_paciente"));
                s.setNomeConvenio(rs.getString("nome_convenio")); // <--- NOVO
                s.setData(rs.getString("data"));
                s.setHora(rs.getString("hora"));
                s.setStatus(rs.getString("status"));
                s.setObservacao(rs.getString("observacao"));
                lista.add(s);
            }
        } catch(Exception e) { e.printStackTrace(); }
        return lista;
    }
    public List<Sessao> listarPorPaciente(Long idPaciente) {
        // Ordena por data (gambiarra do substr para dd/MM/yyyy) e hora decrescente
        String sql = "SELECT * FROM sessao WHERE id_paciente = ? ORDER BY substr(data, 7, 4) || substr(data, 4, 2) || substr(data, 1, 2) DESC, hora DESC";

        List<Sessao> lista = new ArrayList<>();

        try (java.sql.Connection conn = ConexaoFactory.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idPaciente);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sessao s = new Sessao();
                s.setId(rs.getLong("id"));
                s.setIdPaciente(rs.getLong("id_paciente"));
                s.setIdConvenio(rs.getLong("id_convenio")); // Importante pegar isso agora
                s.setData(rs.getString("data"));
                s.setHora(rs.getString("hora"));
                s.setStatus(rs.getString("status"));
                s.setObservacao(rs.getString("observacao"));

                lista.add(s);
            }

        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico: " + e.getMessage());
        }
        return lista;
    }
}
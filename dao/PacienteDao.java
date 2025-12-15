package br.com.meuconsultorio.dao;

import br.com.meuconsultorio.infra.ConexaoFactory;
import br.com.meuconsultorio.model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

    // 1. CRIA A TABELA
    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS paciente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    cpf TEXT,
                    data_nascimento TEXT,
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

    // 2. CADASTRA O PACIENTE
    public void cadastrar(Paciente p) {
        String sql = "INSERT INTO paciente (nome, cpf, data_nascimento, telefone, endereco, historico_geral) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getData_nascimento());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEndereco());
            ps.setString(6, p.getHistoricoGeral());

            ps.execute();
            System.out.println("✅ SUCESSO: Paciente " + p.getNome() + " salvo no banco!");

        } catch (SQLException e) {
            // AQUI ESTÁ O SEGREDO: Se der erro, ele GRITA no console em vermelho
            e.printStackTrace();
            throw new RuntimeException("❌ ERRO AO CADASTRAR: " + e.getMessage());
        }
    }

    // 3. LISTA OS PACIENTES
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM paciente";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setData_nascimento(rs.getString("data_nascimento"));
                p.setTelefone(rs.getString("telefone"));
                p.setEndereco(rs.getString("endereco"));
                p.setHistoricoGeral(rs.getString("historico_geral"));

                pacientes.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes: " + e.getMessage());
        }

        return pacientes;
    }

    // 4. ATUALIZAR DADOS (Salvar edições)
    public void atualizar(Paciente p) {
        String sql = "UPDATE paciente SET nome=?, cpf=?, telefone=?, data_nascimento=?, endereco=?, historico_geral=? WHERE id=?";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getData_nascimento());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEndereco());
            ps.setString(6, p.getHistoricoGeral());
            // O ID é o critério (WHERE), então é o último parametro (7)
            ps.setLong(7, p.getId());

            ps.execute();
            System.out.println("✅ SUCESSO: Paciente atualizado!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    // 5. BUSCAR POR ID (Novo!)
    public Paciente buscarPorId(Long id) {
        String sql = "SELECT * FROM paciente WHERE id = ?";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setData_nascimento(rs.getString("data_nascimento"));
                p.setTelefone(rs.getString("telefone"));
                p.setEndereco(rs.getString("endereco"));
                p.setHistoricoGeral(rs.getString("historico_geral"));
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Se não achar ninguem
    }

    // Metodo Novo: Buscar por Nome (Filtro)
    public List<Paciente> listarPorNome(String parteNome) {
        // O operador LIKE %texto% busca qualquer coisa que contenha o texto
        String sql = "SELECT * FROM paciente WHERE nome LIKE ?";
        List<Paciente> lista = new ArrayList<>();

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + parteNome + "%"); // Adiciona os coringas %
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
                // ... preencha os outros campos se precisar ...
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    // 6. EXCLUIR PACIENTE
    public void excluir(Long id) {
        String sql = "DELETE FROM paciente WHERE id = ?";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir paciente: " + e.getMessage());
        }
    }

}
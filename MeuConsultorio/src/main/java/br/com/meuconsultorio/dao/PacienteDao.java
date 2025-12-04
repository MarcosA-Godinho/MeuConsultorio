package br.com.meuconsultorio.dao;

import br.com.meuconsultorio.infra.ConexaoFactory;
import br.com.meuconsultorio.model.Paciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

    //METODO PARA CRIAR A TABELA NO BANCO (SÓ RODA UMA VEZ)
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

    public void cadastrar(Paciente p) {
        String sql = "INSERT INTO paciente (nome, cpf, data_nascimento, telefone, endereco, historico_geral) VALUES (?,?,?,?,?,?)";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getData_nascimento());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEndereco());
            ps.setString(6, p.getHistoricoGeral());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar paciente: " + e.getMessage());
        }
    }

    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM paciente";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Enquanto tiver uma próxima linha na tabela do banco...
            while (rs.next()) {
                Paciente p = new Paciente();
                // Pegamos os dados da coluna e colocamos no objeto
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
                p.setData_nascimento(rs.getString("data_nascimento"));
                p.setEndereco(rs.getString("endereco"));
                p.setHistoricoGeral(rs.getString("historico_geral"));

                // Adiciona na lista
                pacientes.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes: " + e.getMessage());
        }

        return pacientes;
    }
}

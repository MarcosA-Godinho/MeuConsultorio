package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TelaListaPacientes extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private List<Paciente> listaPacientes;

    public TelaListaPacientes() {
        setTitle("Lista de Pacientes");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 1. CONFIGURAÇÃO DA TABELA (TRAVADA)
        // Aqui está o segredo para não deixar editar na grade:
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // <--- TRAVA A EDIÇÃO
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("CPF");
        modelo.addColumn("Telefone");

        tabela = new JTable(modelo);
        JScrollPane painelRolagem = new JScrollPane(tabela);
        painelRolagem.setBounds(20, 20, 590, 350);
        add(painelRolagem);

        // 2. BOTÕES
        JButton btnAbrir = new JButton("Abrir Prontuário");
        btnAbrir.setBounds(20, 390, 150, 40);
        add(btnAbrir);

        JButton btnRecarregar = new JButton("Recarregar");
        btnRecarregar.setBounds(180, 390, 130, 40);
        add(btnRecarregar);

        JButton btnAgendar = new JButton("Agendar Sessão");
        btnAgendar.setBounds(320, 390, 150, 40);
        add(btnAgendar);

        // 3. AÇÕES
        btnAbrir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela!");
            } else {
                Paciente p = listaPacientes.get(linha);
                new TelaProntuario(p).setVisible(true);
            }
        });

        btnRecarregar.addActionListener(e -> carregarDados());

        btnAgendar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente para agendar!");
            } else {
                Paciente p = listaPacientes.get(linha);
                new TelaAgendamento(p).setVisible(true);
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            PacienteDao dao = new PacienteDao();
            listaPacientes = dao.listarTodos();

            modelo.setRowCount(0);

            for (Paciente p : listaPacientes) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getCpf(),
                        p.getTelefone()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + e.getMessage());
        }
    }
}
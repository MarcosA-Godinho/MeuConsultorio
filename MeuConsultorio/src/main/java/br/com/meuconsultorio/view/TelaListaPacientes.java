package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TelaListaPacientes extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private List<Paciente> listaPacientes; // Guardamos a lista original aqui

    public TelaListaPacientes() {
        setTitle("Lista de Pacientes");
        setSize(600, 500); // Aumentei um pouco a altura
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 1. TABELA
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("CPF");
        modelo.addColumn("Telefone");

        tabela = new JTable(modelo);
        JScrollPane painelRolagem = new JScrollPane(tabela);
        painelRolagem.setBounds(20, 20, 540, 350);
        add(painelRolagem);

        // 2. BOTÃO ABRIR PRONTUÁRIO
        JButton btnAbrir = new JButton("Abrir Prontuário / Editar");
        btnAbrir.setBounds(20, 390, 200, 40);
        add(btnAbrir);

        // 3. BOTÃO ATUALIZAR LISTA (Para ver novos cadastros)
        JButton btnRecarregar = new JButton("Recarregar Lista");
        btnRecarregar.setBounds(230, 390, 150, 40);
        add(btnRecarregar);

        //4. BOTÃO AGENDAR SESSÃO
        JButton btnAgendar = new JButton("Agendar Sessão");
        btnAgendar.setBounds(390, 390, 170, 40); // Ajustei o X para ficar ao lado
        add(btnAgendar);

        // AÇÃO DO BOTÃO ABRIR
        btnAgendar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente!");
            } else {
                // Pega o paciente da lista usando a linha selecionada
                Paciente p = listaPacientes.get(linha);

                // Abre a tela de agendamento
                new TelaAgendamento(p).setVisible(true);
            }
        });

        // AÇÃO DO BOTÃO RECARREGAR
        btnRecarregar.addActionListener(e -> carregarDados());

        carregarDados();
    }

    private void carregarDados() {
        try {
            PacienteDao dao = new PacienteDao();
            listaPacientes = dao.listarTodos(); // Busca e guarda na variavel da classe

            // Limpa a tabela visual
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
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}
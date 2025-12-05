package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TelaAgendaDiaria extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JFormattedTextField txtData;

    public TelaAgendaDiaria() {
        setTitle("Agenda Diária");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 1. FILTRO DE DATA (Topo da tela)
        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        lblData.setBounds(20, 20, 150, 20);
        add(lblData);

        // Já vem preenchido com a data de hoje para facilitar
        String hoje = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(mascaraData);
            txtData.setText(hoje); // Aplica a data de hoje na máscara
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtData.setBounds(20, 45, 100, 25); // Ajuste a largura se precisar
        add(txtData);

        // 2. A TABELA DE HORÁRIOS
        modelo = new DefaultTableModel();
        modelo.addColumn("Hora");
        modelo.addColumn("Paciente");
        modelo.addColumn("Status");
        modelo.addColumn("Observação");

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(20, 90, 540, 350);
        add(scroll);

        // AÇÃO DO BOTÃO FILTRAR
        btnFilString hoje = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(mascaraData);
            txtData.setText(hoje); // Aplica a data de hoje na máscara
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtData.setBounds(20, 45, 100, 25); // Ajuste a largura se precisar
        add(txtData);btnFilString.addActionListener(e -> carregarAgenda());

        // Carrega automaticamente ao abrir a tela
        carregarAgenda();
    }

    private void carregarAgenda() {
        String dataBuscada = txtData.getText();

        try {
            SessaoDao dao = new SessaoDao();
            // IMPORTANTE: O método listarPorData faz o JOIN para pegar o nome do paciente
            List<Sessao> lista = dao.listarPorData(dataBuscada);

            // Limpa a tabela visual
            modelo.setRowCount(0);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum agendamento para esta data.");
            }

            for (Sessao s : lista) {
                modelo.addRow(new Object[]{
                        s.getHora(),
                        s.getNomePaciente(), // <--- Aqui aparece o nome do João, não o ID 1
                        s.getStatus(),
                        s.getObservacao()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar agenda: " + e.getMessage());
        }
    }
}
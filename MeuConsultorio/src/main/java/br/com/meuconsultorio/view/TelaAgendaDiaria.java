package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter; // Importante para a máscara funcionar
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TelaAgendaDiaria extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JFormattedTextField txtData; // Agora é campo formatado

    public TelaAgendaDiaria() {
        setTitle("Agenda Diária");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 1. FILTRO DE DATA
        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        lblData.setBounds(20, 20, 150, 20);
        add(lblData);

        // --- MÁSCARA NOVA (Aula 10) ---
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(mascaraData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Preenche com a data de hoje
        String hoje = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        txtData.setText(hoje);

        txtData.setBounds(20, 45, 120, 25);
        add(txtData);

        // --- BOTÃO FILTRAR (O que estava faltando) ---
        JButton btnFiltrar = new JButton("Filtrar / Atualizar");
        btnFiltrar.setBounds(160, 45, 150, 25);
        add(btnFiltrar);

        // 2. TABELA
        modelo = new DefaultTableModel();
        modelo.addColumn("Hora");
        modelo.addColumn("Paciente");
        modelo.addColumn("Status");
        modelo.addColumn("Observação");

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(20, 90, 540, 350);
        add(scroll);

        // AÇÃO DO BOTÃO
        btnFiltrar.addActionListener(e -> carregarAgenda());

        // Carrega automaticamente ao abrir
        carregarAgenda();
    }

    private void carregarAgenda() {
        String dataBuscada = txtData.getText();

        // Se a data estiver incompleta (tiver _), não busca nada para não dar erro
        if (dataBuscada.contains("_")) {
            return;
        }

        try {
            SessaoDao dao = new SessaoDao();
            List<Sessao> lista = dao.listarPorData(dataBuscada);

            modelo.setRowCount(0);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum agendamento para esta data.");
            }

            for (Sessao s : lista) {
                modelo.addRow(new Object[]{
                        s.getHora(),
                        s.getNomePaciente(),
                        s.getStatus(),
                        s.getObservacao()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar agenda: " + e.getMessage());
        }
    }
}
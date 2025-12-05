package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Paciente;
import br.com.meuconsultorio.model.Sessao;
import br.com.meuconsultorio.util.ValidadorData;

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
    private List<Sessao> listaSessoes; // Guarda os objetos da agenda na memória

    public TelaAgendaDiaria() {
        setTitle("Agenda Diária");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 1. FILTROS
        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(20, 20, 100, 20);
        add(lblData);

        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(mascaraData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Data de hoje automática
        String hoje = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        txtData.setText(hoje);
        txtData.setBounds(20, 45, 100, 25);
        add(txtData);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(130, 45, 100, 25);
        add(btnFiltrar);

        // 2. TABELA TRAVADA
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ninguém mexe aqui!
            }
        };

        modelo.addColumn("Hora");
        modelo.addColumn("Paciente");
        modelo.addColumn("Status");
        modelo.addColumn("Observação");

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(20, 90, 590, 300);
        add(scroll);

        // 3. BOTÃO MÁGICO (Abrir Prontuário)
        JButton btnAbrirProntuario = new JButton("Abrir Prontuário do Paciente");
        btnAbrirProntuario.setBounds(20, 400, 250, 40);
        add(btnAbrirProntuario);

        // --- AÇÕES ---

        // Botão Filtrar
        btnFiltrar.addActionListener(e -> carregarAgenda());

        // Botão Abrir Prontuário
        btnAbrirProntuario.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um horário na tabela!");
                return;
            }

            // 1. Descobre qual é a SESSÃO selecionada
            Sessao sessaoSelecionada = listaSessoes.get(linha);

            // 2. Descobre qual é o ID do paciente dessa sessão
            Long idPaciente = sessaoSelecionada.getIdPaciente();

            // 3. Busca o paciente completo no banco (Usando o método novo do Passo 1)
            PacienteDao pacienteDao = new PacienteDao();
            Paciente p = pacienteDao.buscarPorId(idPaciente);

            // 4. Abre a tela
            if (p != null) {
                new TelaProntuario(p).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Paciente não encontrado!");
            }
        });

        // Carrega ao iniciar
        carregarAgenda();
    }

    private void carregarAgenda() {
        String dataBuscada = txtData.getText();
        if (dataBuscada.contains("_")) return;

        if (!ValidadorData.isDataValida(dataBuscada)) {
            JOptionPane.showMessageDialog(this, "Data inválida!");
            return;
        }

        try {
            SessaoDao dao = new SessaoDao();
            // Guarda na variável da classe para usarmos no botão de Prontuário
            listaSessoes = dao.listarPorData(dataBuscada);

            modelo.setRowCount(0);

            if (listaSessoes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Agenda vazia para esta data.");
            }

            for (Sessao s : listaSessoes) {
                modelo.addRow(new Object[]{
                        s.getHora(),
                        s.getNomePaciente(),
                        s.getStatus(),
                        s.getObservacao()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}
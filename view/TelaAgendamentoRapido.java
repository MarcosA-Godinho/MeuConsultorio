package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Paciente;
import br.com.meuconsultorio.model.Sessao;
import br.com.meuconsultorio.util.ValidadorData;
import br.com.meuconsultorio.util.ValidadorHora;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.util.List;

public class TelaAgendamentoRapido extends JFrame {

    private JComboBox<Paciente> cmbPacientes;
    private JFormattedTextField txtData;
    private JFormattedTextField txtHora;

    public TelaAgendamentoRapido() {
        setTitle("Agendamento Rápido");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 1. ESCOLHER PACIENTE
        JLabel lblPac = new JLabel("Selecione o Paciente:");
        lblPac.setBounds(20, 20, 200, 20);
        add(lblPac);

        cmbPacientes = new JComboBox<>();
        cmbPacientes.setBounds(20, 45, 340, 30);
        add(cmbPacientes);

        // Carrega os pacientes do banco para o ComboBox
        carregarPacientes();

        // 2. DATA
        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        lblData.setBounds(20, 90, 150, 20);
        add(lblData);

        try {
            MaskFormatter maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(maskData);
            txtData.setBounds(20, 115, 120, 30);
            add(txtData);
        } catch (Exception e) { e.printStackTrace(); }

        // 3. HORA
        JLabel lblHora = new JLabel("Hora (HH:mm):");
        lblHora.setBounds(200, 90, 150, 20);
        add(lblHora);

        try {
            MaskFormatter maskHora = new MaskFormatter("##:##");
            maskHora.setPlaceholderCharacter('_');
            txtHora = new JFormattedTextField(maskHora);
            txtHora.setBounds(200, 115, 80, 30);
            add(txtHora);
        } catch (Exception e) { e.printStackTrace(); }

        // 4. BOTÃO SALVAR
        JButton btnSalvar = new JButton("Confirmar Agendamento");
        btnSalvar.setBounds(20, 180, 340, 40);
        btnSalvar.setBackground(new java.awt.Color(60, 163, 255)); // Azulzinho
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvar());
    }

    private void carregarPacientes() {
        try {
            PacienteDao dao = new PacienteDao();
            List<Paciente> lista = dao.listarTodos();

            for (Paciente p : lista) {
                cmbPacientes.addItem(p); // Adiciona o objeto inteiro
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar pacientes: " + e.getMessage());
        }
    }

    private void salvar() {
        // Validações
        Paciente pacienteSelecionado = (Paciente) cmbPacientes.getSelectedItem();
        String data = txtData.getText();
        String hora = txtHora.getText();

        if (pacienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente!");
            return;
        }
        if (!ValidadorData.isDataValida(data)) {
            JOptionPane.showMessageDialog(this, "Data inválida!");
            return;
        }
        if (!ValidadorHora.isHoraValida(hora)) {
            JOptionPane.showMessageDialog(this, "Hora inválida!");
            return;
        }

        // Salva no Banco
        Sessao s = new Sessao();
        s.setIdPaciente(pacienteSelecionado.getId());
        s.setData(data);
        s.setHora(hora);

        try {
            new SessaoDao().agendar(s);
            JOptionPane.showMessageDialog(this, "Agendado com sucesso!");
            dispose(); // Fecha a janela
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}
package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Paciente;
import br.com.meuconsultorio.model.Sessao;
import br.com.meuconsultorio.util.ValidadorData;
import br.com.meuconsultorio.util.ValidadorHora;
import br.com.meuconsultorio.dao.ConvenioDao; // Importe o DAO
import br.com.meuconsultorio.model.Convenio; // Importe o Modelo

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.util.List;

public class TelaAgendamentoRapido extends JFrame {

    private JComboBox<Paciente> cmbPacientes;
    private JFormattedTextField txtData;
    private JComboBox<Convenio> cmbConvenios;
    private JFormattedTextField txtHora;

    public TelaAgendamentoRapido() {
        setTitle("Agendamento Rápido");
        setSize(400, 420);
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

        JLabel lblConv = new JLabel("Tipo / Convênio:");
        lblConv.setBounds(20, 80, 200, 20);
        add(lblConv);

        cmbConvenios = new JComboBox<>();
        cmbConvenios.setBounds(20, 105, 340, 30);
        add(cmbConvenios);

        carregarConvenios(); // Chama o método para preencher

        // 2. DATA
        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        lblData.setBounds(20, 180, 150, 20);
        add(lblData);

        try {
            MaskFormatter maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(maskData);
            txtData.setBounds(20, 150, 120, 30);
            add(txtData);
        } catch (Exception e) { e.printStackTrace(); }

        // 3. HORA
        JLabel lblHora = new JLabel("Hora (HH:mm):");
        lblHora.setBounds(200, 180, 150, 20);
        add(lblHora);

        try {
            MaskFormatter maskHora = new MaskFormatter("##:##");
            maskHora.setPlaceholderCharacter('_');
            txtHora = new JFormattedTextField(maskHora);
            txtHora.setBounds(200, 150, 80, 30);
            add(txtHora);
        } catch (Exception e) { e.printStackTrace(); }

        // 4. BOTÃO SALVAR
        JButton btnSalvar = new JButton("Confirmar Agendamento");
        btnSalvar.setBounds(20, 240, 340, 40);
        btnSalvar.setBackground(new java.awt.Color(60, 163, 255)); // Azulzinho
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvar());
    }

    private void carregarConvenios() {
        try {
            List<Convenio> lista = new ConvenioDao().listarTodos();
            for (Convenio c : lista) {
                cmbConvenios.addItem(c);
            }
        } catch (Exception e) { e.printStackTrace(); }
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
        // Validações (permanecem iguais)
        Paciente p = (Paciente) cmbPacientes.getSelectedItem();
        Convenio c = (Convenio) cmbConvenios.getSelectedItem();
        String data = txtData.getText();
        String hora = txtHora.getText();

        if (p == null) {
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
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Selecione um convênio!");
            return;
        }

        // Prepara o objeto
        Sessao s = new Sessao();
        s.setIdPaciente(p.getId());
        s.setIdConvenio(c.getId());
        s.setData(data);
        s.setHora(hora);

        try {
            SessaoDao dao = new SessaoDao();

            // --- A CORREÇÃO ESTÁ AQUI ---
            dao.criarTabela(); // Garante que a tabela 'sessao' será criada se não existir
            // ---------------------------

            dao.agendar(s);

            JOptionPane.showMessageDialog(this, "Agendado com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao agendar: " + ex.getMessage());
            ex.printStackTrace(); // Ajuda a ver o erro no console
        }
    }
}
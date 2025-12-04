package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Paciente;
import br.com.meuconsultorio.model.Sessao;

import javax.swing.*;

public class TelaAgendamento extends JFrame {

    private JTextField txtData;
    private JTextField txtHora;
    private Paciente paciente;

    public TelaAgendamento(Paciente p) {
        this.paciente = p;

        setTitle("Agendar: " + p.getNome());
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        lblData.setBounds(20, 20, 200, 20);
        add(lblData);

        txtData = new JTextField();
        txtData.setBounds(20, 45, 240, 25);
        add(txtData);

        JLabel lblHora = new JLabel("Hora (HH:mm):");
        lblHora.setBounds(20, 80, 200, 20);
        add(lblHora);

        txtHora = new JTextField();
        txtHora.setBounds(20, 105, 100, 25);
        add(txtHora);

        JButton btnSalvar = new JButton("Confirmar Agendamento");
        btnSalvar.setBounds(20, 150, 240, 40);
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvar());
    }

    private void salvar() {
        Sessao s = new Sessao();
        s.setIdPaciente(paciente.getId()); // O VÍNCULO IMPORTANTE!
        s.setData(txtData.getText());
        s.setHora(txtHora.getText());

        try {
            SessaoDao dao = new SessaoDao();
            // IMPORTANTE: Garantir que a tabela existe antes de agendar
            dao.criarTabela();

            dao.agendar(s);

            JOptionPane.showMessageDialog(this, "Agendado com sucesso!");
            dispose(); // Fecha a janelinha
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}
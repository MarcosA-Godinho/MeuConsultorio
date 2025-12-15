package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.model.Paciente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaProntuario extends JFrame {

    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextArea txtHistorico; // Campo de texto grande
    private Paciente pacienteAtual; // O paciente que estamos editando

    // CONSTRUTOR: Recebe o paciente que foi selecionado na outra tela!
    public TelaProntuario(Paciente p) {
        this.pacienteAtual = p; // Guarda o paciente na memória da tela

        setTitle("Prontuário: " + p.getNome());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // --- DADOS BÁSICOS (Só leitura ou edição leve) ---
        JLabel lblNome = new JLabel("Paciente:");
        lblNome.setBounds(20, 20, 100, 20);
        add(lblNome);

        txtNome = new JTextField(p.getNome());
        txtNome.setBounds(20, 45, 300, 25);
        add(txtNome);

        JLabel lblTel = new JLabel("Telefone:");
        lblTel.setBounds(340, 20, 100, 20);
        add(lblTel);

        txtTelefone = new JTextField(p.getTelefone());
        txtTelefone.setBounds(340, 45, 200, 25);
        add(txtTelefone);

        // --- O PRONTUÁRIO (Campo Gigante) ---
        JLabel lblHist = new JLabel("Histórico / Anotações Gerais:");
        lblHist.setBounds(20, 90, 200, 20);
        add(lblHist);

        txtHistorico = new JTextArea(p.getHistoricoGeral());
        txtHistorico.setLineWrap(true); // Quebra a linha automaticamente
        txtHistorico.setWrapStyleWord(true); // Não corta palavras no meio

        // Scroll para o texto longo
        JScrollPane scroll = new JScrollPane(txtHistorico);
        scroll.setBounds(20, 115, 540, 250);
        add(scroll);

        // --- BOTÃO SALVAR ---
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBounds(20, 390, 540, 40);
        add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            }
        });
    }

    private void salvarAlteracoes() {
        // 1. Atualiza o objeto com o que está na tela
        pacienteAtual.setNome(txtNome.getText());
        pacienteAtual.setTelefone(txtTelefone.getText());
        pacienteAtual.setHistoricoGeral(txtHistorico.getText()); // Pega o textão

        // 2. Manda pro banco
        try {
            PacienteDao dao = new PacienteDao();
            dao.atualizar(pacienteAtual); // Chama o método UPDATE

            JOptionPane.showMessageDialog(this, "Prontuário atualizado com sucesso!");
            dispose(); // Fecha a janela do prontuário

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}
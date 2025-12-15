package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.model.Paciente;
import br.com.meuconsultorio.util.ValidadorCpf; // <--- Não esqueça desse import!

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastroPaciente extends JFrame {

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtTelefone;
    private JButton btnSalvar;

    public TelaCadastroPaciente() {
        setTitle("Meu Consultório - Cadastro");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só a janela
        setLayout(null);
        setLocationRelativeTo(null);

        // --- NOME ---
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setBounds(20, 20, 150, 20);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(20, 45, 340, 25);
        add(txtNome);

        // --- CPF ---
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(20, 80, 100, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(20, 105, 150, 25);
        add(txtCpf);

        // --- TELEFONE ---
        JLabel lblTel = new JLabel("Telefone:");
        lblTel.setBounds(190, 80, 100, 20);
        add(lblTel);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(190, 105, 170, 25);
        add(txtTelefone);

        // --- BOTÃO ---
        btnSalvar = new JButton("Salvar Paciente");
        btnSalvar.setBounds(20, 160, 340, 40);
        add(btnSalvar);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarNoBanco();
            }
        });
    }

    private void salvarNoBanco() {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String telefone = txtTelefone.getText();

        // Validação 1: Nome
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Erro: O nome é obrigatório!");
            return;
        }

        // Validação 2: CPF (Item 1 da sua lista)
        // O return tem que estar DENTRO do if
        if (!ValidadorCpf.isCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "Erro: CPF inválido!");
            return;
        }

        // Se passou pelos returns acima, o código chega aqui (agora é alcançável!)
        Paciente p = new Paciente();
        p.setNome(nome);
        p.setCpf(cpf);
        p.setTelefone(telefone);

        try {
            PacienteDao dao = new PacienteDao();
            dao.cadastrar(p);

            JOptionPane.showMessageDialog(this, "Sucesso! Paciente cadastrado.");

            txtNome.setText("");
            txtCpf.setText("");
            txtTelefone.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }
}
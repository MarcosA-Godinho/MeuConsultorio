package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.model.Paciente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCadastroPaciente extends JFrame {

    //COMPONENTES DA TELA (CAMPOS DE TEXTO E BOTÃO)
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtTelefone;
    private JButton btnSalvar;

    public TelaCadastroPaciente() {

        //1°CONFIGURAÇÃO DA JANELA
        setTitle("Meu Consultorio - Cadastro");
        setSize(400, 300); //TAMANHO: LARGURA 400, ALTURA 300
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//FECHA O PROGRAMA AO FECHAR A JANELA
        setLayout(null);//LAYOUT MANUAL (VAMOS DEFINIR O X E Y DE CADA COISA
        setLocationRelativeTo(null);//CENTRALZIA A JANELA NA TELA

        //2°CRIANDO OS COMPONENTES
        //----CAMPO NOME----
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setBounds(20, 20, 150, 20); //X, Y , largura, altura
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(20, 45, 340, 25);
        add(txtNome);

        // --- Campo CPF ---
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(20, 80, 100, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(20, 105, 150, 25);
        add(txtCpf);

        // --- Campo TELEFONE ---
        JLabel lblTel = new JLabel("Telefone:");
        lblTel.setBounds(190, 80, 100, 20);
        add(lblTel);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(190, 105, 170, 25);
        add(txtTelefone);

        // --- BOTÃO SALVAR ---
        btnSalvar = new JButton("Salvar Paciente");
        btnSalvar.setBounds(20, 160, 340, 40); // Botão grandão
        add(btnSalvar);

        // 3. AÇÃO DO BOTÃO (O CÉREBRO DA TELA)
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarNoBanco();
            }
        });
    }

    // Metodo que pega os dados da tela e manda pro DAO
    private void salvarNoBanco() {
        // Pega o texto que a pessoa digitou
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String telefone = txtTelefone.getText();

        // Validação simples: Nome é obrigatório
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Erro: O nome é obrigatório!");
            return; // Para o código aqui
        }

        // Cria o objeto Paciente
        Paciente p = new Paciente();
        p.setNome(nome);
        p.setCpf(cpf);
        p.setTelefone(telefone);
        // Obs: Como não colocamos campos para Endereço/Data na tela ainda,
        // eles irão vazios (null) para o banco. Tudo bem por enquanto!

        // Tenta salvar usando o DAO
        try {
            PacienteDao dao = new PacienteDao();
            dao.cadastrar(p);

            JOptionPane.showMessageDialog(this, "Sucesso! Paciente cadastrado.");

            // Limpa os campos para digitar o próximo
            txtNome.setText("");
            txtCpf.setText("");
            txtTelefone.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }
}


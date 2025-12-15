package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Paciente;
import br.com.meuconsultorio.model.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TelaPerfilPaciente extends JFrame {

    private Paciente paciente;
    private JTextField txtNome, txtCpf, txtTelefone;
    private JTextArea txtHistorico;
    private JTable tabelaHistorico;
    private DefaultTableModel modeloHistorico;

    public TelaPerfilPaciente(Paciente p) {
        this.paciente = p;

        setTitle("Perfil do Paciente: " + p.getNome());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // O COMPONENTE DE ABAS
        JTabbedPane abas = new JTabbedPane();

        // Criação das 3 abas
        abas.addTab("Dados Pessoais", criarPainelDados());
        abas.addTab("Prontuário Geral", criarPainelProntuario());
        abas.addTab("Histórico de Consultas", criarPainelHistorico());

        add(abas);
    }

    // --- ABA 1: DADOS PESSOAIS + EXCLUIR ---
    private JPanel criarPainelDados() {
        JPanel painel = new JPanel(null); // Layout manual

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 20, 100, 20);
        painel.add(lblNome);

        txtNome = new JTextField(paciente.getNome());
        txtNome.setBounds(20, 45, 300, 25);
        painel.add(txtNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(20, 80, 100, 20);
        painel.add(lblCpf);

        txtCpf = new JTextField(paciente.getCpf());
        txtCpf.setBounds(20, 105, 150, 25);
        txtCpf.setEditable(false); // CPF não se muda!
        painel.add(txtCpf);

        JLabel lblTel = new JLabel("Telefone:");
        lblTel.setBounds(190, 80, 100, 20);
        painel.add(lblTel);

        txtTelefone = new JTextField(paciente.getTelefone());
        txtTelefone.setBounds(190, 105, 130, 25);
        painel.add(txtTelefone);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setBounds(20, 160, 300, 40);
        painel.add(btnSalvar);

        // Botão Excluir (Vermelho e Perigoso)
        JButton btnExcluir = new JButton("EXCLUIR PACIENTE");
        btnExcluir.setBounds(20, 350, 300, 40);
        btnExcluir.setBackground(new java.awt.Color(255, 100, 100)); // Vermelhinho
        painel.add(btnExcluir);

        // Ações
        btnSalvar.addActionListener(e -> {
            paciente.setNome(txtNome.getText());
            paciente.setTelefone(txtTelefone.getText());
            new PacienteDao().atualizar(paciente);
            JOptionPane.showMessageDialog(this, "Dados atualizados!");
        });

        btnExcluir.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Tem certeza? Isso apagará o paciente e todo o histórico dele!",
                    "Cuidado", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                new PacienteDao().excluir(paciente.getId());
                JOptionPane.showMessageDialog(this, "Paciente excluído.");
                dispose(); // Fecha a tela
            }
        });

        return painel;
    }

    // --- ABA 2: PRONTUÁRIO (Textão) ---
    private JPanel criarPainelProntuario() {
        JPanel painel = new JPanel(null);

        JLabel lblAviso = new JLabel("Anotações Gerais / Ficha de Evolução:");
        lblAviso.setBounds(20, 20, 300, 20);
        painel.add(lblAviso);

        txtHistorico = new JTextArea(paciente.getHistoricoGeral());
        txtHistorico.setLineWrap(true);
        txtHistorico.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtHistorico);
        scroll.setBounds(20, 50, 640, 300);
        painel.add(scroll);

        JButton btnSalvarProntuario = new JButton("Salvar Evolução");
        btnSalvarProntuario.setBounds(20, 360, 640, 40);
        painel.add(btnSalvarProntuario);

        btnSalvarProntuario.addActionListener(e -> {
            paciente.setHistoricoGeral(txtHistorico.getText());
            new PacienteDao().atualizar(paciente);
            JOptionPane.showMessageDialog(this, "Prontuário salvo!");
        });

        return painel;
    }

    // --- ABA 3: HISTÓRICO DE CONSULTAS (Tabela) ---
    private JPanel criarPainelHistorico() {
        JPanel painel = new JPanel(null);

        modeloHistorico = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Trava a edição!
            }
        };
        modeloHistorico.addColumn("Data");
        modeloHistorico.addColumn("Hora");
        modeloHistorico.addColumn("Status");
        modeloHistorico.addColumn("Obs da Sessão");

        tabelaHistorico = new JTable(modeloHistorico);
        JScrollPane scroll = new JScrollPane(tabelaHistorico);
        scroll.setBounds(20, 20, 640, 380);
        painel.add(scroll);

        // Carrega os dados
        try {
            List<Sessao> sessoes = new SessaoDao().listarPorPaciente(paciente.getId());
            for (Sessao s : sessoes) {
                modeloHistorico.addRow(new Object[]{
                        s.getData(), s.getHora(), s.getStatus(), s.getObservacao()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return painel;
    }
}
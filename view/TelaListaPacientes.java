package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TelaListaPacientes extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private List<Paciente> listaPacientes;
    private JTextField txtBusca; // Campo novo

    public TelaListaPacientes() {
        setTitle("Lista de Pacientes");
        setSize(650, 550); // Aumentei um pouco a altura
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // --- ÁREA DE BUSCA (NOVO) ---
        JLabel lblBusca = new JLabel("Buscar por Nome:");
        lblBusca.setBounds(20, 20, 120, 25);
        add(lblBusca);

        txtBusca = new JTextField();
        txtBusca.setBounds(130, 20, 300, 25);
        add(txtBusca);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(440, 20, 100, 25);
        add(btnBuscar);

        // --- TABELA ---
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("CPF");
        modelo.addColumn("Telefone");

        tabela = new JTable(modelo);
        JScrollPane painelRolagem = new JScrollPane(tabela);
        painelRolagem.setBounds(20, 60, 590, 350); // Desci um pouco o Y
        add(painelRolagem);

        // --- BOTÕES DE AÇÃO (Rodapé) ---
        JButton btnAbrir = new JButton("Abrir Perfil / Editar");
        btnAbrir.setBounds(20, 430, 180, 40);
        add(btnAbrir);

        JButton btnAgendar = new JButton("Agendar Sessão");
        btnAgendar.setBounds(210, 430, 150, 40);
        add(btnAgendar);

        JButton btnRecarregar = new JButton("Limpar Filtro");
        btnRecarregar.setBounds(370, 430, 150, 40);
        add(btnRecarregar);


        // --- AÇÕES ---

        // Ação Buscar (Item 2)
        btnBuscar.addActionListener(e -> pesquisar());

        // Ação Limpar (Volta a mostrar tudo)
        btnRecarregar.addActionListener(e -> {
            txtBusca.setText("");
            carregarDados(""); // Busca vazia = Trazer todos
        });

        // Ação Perfil (Itens 3, 4, 5 - Preparação)
        btnAbrir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente!");
                return;
            }
            Paciente p = listaPacientes.get(linha);

            // AQUI VAMOS CHAMAR A NOVA TELA DE PERFIL NA PRÓXIMA ETAPA
            // Por enquanto, deixe chamando o Prontuário antigo ou um aviso
            new TelaPerfilPaciente(p).setVisible(true);
        });

        // Ação Agendar
        btnAgendar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente!");
                return;
            }
            Paciente p = listaPacientes.get(linha);
            new TelaAgendamento(p).setVisible(true);
        });

        // Carrega tudo ao iniciar
        carregarDados("");
    }

    private void pesquisar() {
        String nome = txtBusca.getText();
        carregarDados(nome);
    }

    private void carregarDados(String filtroNome) {
        try {
            PacienteDao dao = new PacienteDao();

            if (filtroNome.isEmpty()) {
                listaPacientes = dao.listarTodos(); // Traz tudo
            } else {
                listaPacientes = dao.listarPorNome(filtroNome); // Traz só o filtro
            }

            modelo.setRowCount(0);
            for (Paciente p : listaPacientes) {
                modelo.addRow(new Object[]{ p.getId(), p.getNome(), p.getCpf(), p.getTelefone() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}
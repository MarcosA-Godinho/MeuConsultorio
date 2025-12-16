package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private JTable tabelaDashboard;
    private DefaultTableModel modeloDashboard;
    private JComboBox<String> cmbFiltro;
    private List<Sessao> listaAtual; // Para sabermos qual ID alterar

    public TelaPrincipal() {
        setTitle("Meu Consultório - Painel de Controle");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        criarMenu();
        criarDashboard();
    }

    private void criarMenu() {
        JMenuBar barraMenu = new JMenuBar();
        setJMenuBar(barraMenu);

        // --- Cadastros ---
        JMenu menuCadastros = new JMenu("Cadastros");
        barraMenu.add(menuCadastros);

        JMenuItem itemNovo = new JMenuItem("Novo Paciente");
        JMenuItem itemLista = new JMenuItem("Listar Pacientes");
        JMenuItem itemConvenios = new JMenuItem("Convênios / Preços"); // AULA 12

        menuCadastros.add(itemNovo);
        menuCadastros.add(itemLista);
        menuCadastros.add(itemConvenios);

        // --- Agenda ---
        JMenu menuAgenda = new JMenu("Agenda");
        barraMenu.add(menuAgenda);
        JMenuItem itemAgendaDiaria = new JMenuItem("Agenda Diária");
        menuAgenda.add(itemAgendaDiaria);

        // --- Opções ---
        JMenu menuOpcoes = new JMenu("Opções");
        barraMenu.add(menuOpcoes);
        JMenuItem itemSair = new JMenuItem("Sair");
        menuOpcoes.add(itemSair);

        // AÇÕES
        itemNovo.addActionListener(e -> new TelaCadastroPaciente().setVisible(true));
        itemLista.addActionListener(e -> new TelaListaPacientes().setVisible(true));
        itemConvenios.addActionListener(e -> new TelaConvenios().setVisible(true));
        itemAgendaDiaria.addActionListener(e -> new TelaAgendaDiaria().setVisible(true));
        itemSair.addActionListener(e -> System.exit(0));
    }

    private void criarDashboard() {
        // Título
        JLabel lblTitulo = new JLabel("Painel de Atendimentos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(30, 20, 300, 30);
        add(lblTitulo);

        // FILTRO (COMBOBOX)
        JLabel lblFiltro = new JLabel("Exibir:");
        lblFiltro.setBounds(450, 25, 50, 20);
        add(lblFiltro);

        String[] opcoes = {"Agendado", "Realizado", "Cancelado", "Todos"};
        cmbFiltro = new JComboBox<>(opcoes);
        cmbFiltro.setBounds(500, 20, 150, 30);
        add(cmbFiltro);

        JButton btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setBounds(660, 20, 120, 30);
        add(btnAtualizar);

        // TABELA
        modeloDashboard = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modeloDashboard.addColumn("Data");
        modeloDashboard.addColumn("Hora");
        modeloDashboard.addColumn("Paciente");
        modeloDashboard.addColumn("Tipo");
        modeloDashboard.addColumn("Status");

        tabelaDashboard = new JTable(modeloDashboard);
        JScrollPane scroll = new JScrollPane(tabelaDashboard);
        scroll.setBounds(30, 60, 750, 380);
        add(scroll);

        // --- BOTÕES DE AÇÃO RÁPIDA (RODAPÉ) ---
        JButton btnConcluir = new JButton("✅ Realizado");
        btnConcluir.setBounds(560, 460, 110, 40);
        btnConcluir.setBackground(new Color(144, 238, 144)); // Verde
        add(btnConcluir);

        JButton btnCancelar = new JButton("❌ Cancelado");
        btnCancelar.setBounds(680, 460, 110, 40);
        btnCancelar.setBackground(new Color(255, 182, 193)); // Vermelho/Rosa
        add(btnCancelar);

        JButton btnAgendar = new JButton("📅 Agendar");
        btnAgendar.setBounds(30, 460, 110, 40);
        btnAgendar.setBackground(new Color(60, 163, 255, 255)); // Vermelho/Rosa
        add(btnAgendar);

        // --- LÓGICA ---

        // Botão Agendar
        btnAgendar.addActionListener(e -> {
            // Abre a tela nova
            TelaAgendamentoRapido tela = new TelaAgendamentoRapido();
            tela.setVisible(true);

            // Dica: Adiciona um "listener" para quando a janela fechar, atualizar o dashboard
            tela.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    carregarDashboard(); // Atualiza a tabela assim que terminar de agendar
                }
            });
        });

        // Ao mudar o filtro no ComboBox, recarrega a tabela automaticamente
        cmbFiltro.addActionListener(e -> carregarDashboard());

        // Botão Atualizar
        btnAtualizar.addActionListener(e -> carregarDashboard());

        // Botão Concluir
        btnConcluir.addActionListener(e -> alterarStatus("Realizado"));

        // Botão Cancelar
        btnCancelar.addActionListener(e -> alterarStatus("Cancelado"));

        // Carrega ao abrir
        carregarDashboard();
    }

    private void carregarDashboard() {
        try {
            modeloDashboard.setRowCount(0);

            // Pega o que está selecionado no Combo (Ex: "Agendado")
            String filtro = (String) cmbFiltro.getSelectedItem();

            // Busca no banco com filtro
            listaAtual = new SessaoDao().listarPorFiltro(filtro);

            for (Sessao s : listaAtual) {
                modeloDashboard.addRow(new Object[]{
                        s.getData(), s.getHora(), s.getNomePaciente(), s.getNomeConvenio(),s.getStatus()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alterarStatus(String novoStatus) {
        int linha = tabelaDashboard.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um atendimento na tabela acima!");
            return;
        }

        Sessao s = listaAtual.get(linha);

        // Pergunta de segurança
        int resp = JOptionPane.showConfirmDialog(this,
                "Mudar status de " + s.getNomePaciente() + " para " + novoStatus + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            new SessaoDao().atualizarStatus(s.getId(), novoStatus);
            carregarDashboard(); // Atualiza a tabela (O item vai sumir ou mudar, dependendo do filtro)
        }
    }
}

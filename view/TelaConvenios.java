package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.ConvenioDao;
import br.com.meuconsultorio.model.Convenio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TelaConvenios extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaConvenios() {
        setTitle("Gerenciar Convênios e Preços");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JButton btnNovo = new JButton("Cadastrar Novo Convênio");
        btnNovo.setBounds(20, 20, 200, 30);
        add(btnNovo);

        modelo = new DefaultTableModel();
        modelo.addColumn("Nome");
        modelo.addColumn("Valor Padrão (R$)");

        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(20, 60, 440, 280);
        add(scroll);

        // AÇÃO DO BOTÃO
        btnNovo.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog("Nome do Convênio (ex: Unimed):");
            if (nome == null || nome.isEmpty()) return;

            String valorStr = JOptionPane.showInputDialog("Valor da Sessão (ex: 80.00):");
            if (valorStr == null || valorStr.isEmpty()) return;

            try {
                // Troca virgula por ponto para não dar erro
                double valor = Double.parseDouble(valorStr.replace(",", "."));

                Convenio c = new Convenio(nome, valor);
                new ConvenioDao().cadastrar(c);
                carregarTabela();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido! Digite apenas números.");
            }
        });

        // Garante criação da tabela no banco ao abrir a tela
        new ConvenioDao().criarTabela();
        carregarTabela();
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        List<Convenio> lista = new ConvenioDao().listarTodos();
        for (Convenio c : lista) {
            modelo.addRow(new Object[]{ c.getNome(), "R$ " + c.getValor() });
        }
    }
}
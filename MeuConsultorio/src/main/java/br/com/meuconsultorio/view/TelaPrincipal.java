package br.com.meuconsultorio.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        // 1. CONFIGURAÇÕES DA JANELA PRINCIPAL
        setTitle("Meu Consultório - Sistema de Gestão");
        setSize(1000, 800); // Uma tela bem maior
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 2. CRIANDO A BARRA DE MENUS
        JMenuBar barraMenu = new JMenuBar();
        setJMenuBar(barraMenu); // "Cola" a barra no topo da janela

        // --- Menu Cadastros ---
        JMenu menuCadastros = new JMenu("Cadastros");
        barraMenu.add(menuCadastros);

        // --- Menu Agenda (NOVO!) ---
        JMenu menuAgenda = new JMenu("Agenda");
        barraMenu.add(menuAgenda);


        JMenuItem itemAgendaDiaria = new JMenuItem("Agenda Diária");
        menuAgenda.add(itemAgendaDiaria);

        // Item 1: Novo Paciente
        JMenuItem itemNovo = new JMenuItem("Novo Paciente");
        menuCadastros.add(itemNovo);

        // Item 2: Listar Pacientes (NOVO!)
        JMenuItem itemLista = new JMenuItem("Listar Pacientes");
        menuCadastros.add(itemLista);

        // --- Menu Sair ---
        JMenu menuOpcoes = new JMenu("Opções");
        barraMenu.add(menuOpcoes);

        JMenuItem itemSair = new JMenuItem("Sair do Sistema");
        menuOpcoes.add(itemSair);

        // 3. AÇÕES DOS MENUS (O QUE ACONTECE QUANDO CLICA)

        // Ação: Clicou em Pacientes -> Abre a tela de cadastro
        itemNovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cria e mostra a tela de cadastro
                TelaCadastroPaciente tela = new TelaCadastroPaciente();
                tela.setVisible(true);

                // Dica: Não usamos 'dispose()' aqui, pois queremos manter
                // a tela principal aberta lá no fundo.
                // Mas, na TelaCadastroPaciente, mude o setDefaultCloseOperation para DISPOSE_ON_CLOSE
                // senão fechar o cadastro fecha o sistema todo! (Vamos ajustar isso jaja)
            }
        });
        // Clicou em Listar -> Abre tela de listagem (NOVO!)
        itemLista.addActionListener(e -> {
            TelaListaPacientes tela = new TelaListaPacientes();
            tela.setVisible(true);
        });

        // Ação: Clicou em Sair -> Fecha tudo
        itemSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Ação: Agenda diaria
        itemAgendaDiaria.addActionListener(e -> {
            new TelaAgendaDiaria().setVisible(true);
        });
    }
}
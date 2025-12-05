package br.com.meuconsultorio.view;

import br.com.meuconsultorio.dao.SessaoDao;
import br.com.meuconsultorio.model.Paciente;
import br.com.meuconsultorio.model.Sessao;
import br.com.meuconsultorio.util.ValidadorData;
import br.com.meuconsultorio.util.ValidadorHora;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class TelaAgendamento extends JFrame {

    // MUDANÇA 1: Agora são JFormattedTextField (Campos Formatados)
    private JFormattedTextField txtData;
    private JFormattedTextField txtHora;
    private Paciente paciente;

    public TelaAgendamento(Paciente p) {
        this.paciente = p;

        setTitle("Agendar: " + p.getNome());
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // --- CONFIGURANDO O CAMPO DATA ---
        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(20, 20, 200, 20);
        add(lblData);

        try {
            // Cria a máscara: ## = número obrigatório
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_'); // Mostra _ onde falta digitar

            txtData = new JFormattedTextField(mascaraData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtData.setBounds(20, 45, 100, 25); // Diminuí a largura pois data é curta
        add(txtData);

        // --- CONFIGURANDO O CAMPO HORA ---
        JLabel lblHora = new JLabel("Hora:");
        lblHora.setBounds(20, 80, 200, 20);
        add(lblHora);

        try {
            // Cria a máscara de hora HH:mm
            MaskFormatter mascaraHora = new MaskFormatter("##:##");
            mascaraHora.setPlaceholderCharacter('_');

            txtHora = new JFormattedTextField(mascaraHora);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtHora.setBounds(20, 105, 60, 25);
        add(txtHora);

        // --- BOTÃO SALVAR ---
        JButton btnSalvar = new JButton("Confirmar Agendamento");
        btnSalvar.setBounds(20, 150, 240, 40);
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvar());
    }

    private void salvar() {
        // MUDANÇA 2: Validação
        // O metodo getText() agora retorna algo como "  /  /    " se estiver vazio
        // Precisamos limpar os caracteres da máscara para checar se tem números

        String dataDigitada = txtData.getText();
        String horaDigitada = txtHora.getText();

        // Verifica se a data é válida (grosseiramente)
        // Se a pessoa não digitou nada, vai estar "__/__/____"
        if (dataDigitada.contains("_")) {
            JOptionPane.showMessageDialog(this, "Erro: Digite a DATA completa!");
            return;
        }
        //VALIDAR SE A DATA É VERDADEIRA
        if (!ValidadorData.isDataValida(dataDigitada)) {
            JOptionPane.showMessageDialog(this, "Erro: Data inexistente! Verifique dia e mês.");
            return;
        }

        if (horaDigitada.contains("_")) {
            JOptionPane.showMessageDialog(this, "Erro: Digite a HORA completa!");
            return;
        }
        //VALIDADOR SE O HORARIO DIGITADO É VERDADEIRO
        if (!ValidadorHora.isHoraValida(horaDigitada)) {
            JOptionPane.showMessageDialog(this, "Erro: Hora inválida! Use formato HH:mm (00:00 a 23:59).");
            return;
        }

        Sessao s = new Sessao();
        s.setIdPaciente(paciente.getId());
        s.setData(dataDigitada);
        s.setHora(horaDigitada);

        try {
            SessaoDao dao = new SessaoDao();
            dao.criarTabela();
            dao.agendar(s);

            JOptionPane.showMessageDialog(this, "Agendado com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}
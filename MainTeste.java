package br.com.meuconsultorio;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.view.TelaPrincipal;

public class MainTeste {
    public static void main(String[] args) {

        // Inicializa o banco (segurança)
        PacienteDao dao = new PacienteDao();
        dao.criarTabela();

        // Abre a Tela Principal (Menu)
        TelaPrincipal tela = new TelaPrincipal();
        tela.setVisible(true);
    }
}
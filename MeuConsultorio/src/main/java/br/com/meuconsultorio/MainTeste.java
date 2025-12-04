package br.com.meuconsultorio;

import br.com.meuconsultorio.dao.PacienteDao;

import java.sql.Connection;

public class MainTeste {
    public static void main(String[] args) {

        //INSTANCIA O DAO
        PacienteDao dao = new PacienteDao();
        //MANDA CRIAR TABELA
        dao.criarTabela();
    }
}
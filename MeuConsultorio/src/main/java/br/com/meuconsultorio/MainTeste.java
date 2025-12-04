package br.com.meuconsultorio;

import br.com.meuconsultorio.dao.PacienteDao;
import br.com.meuconsultorio.model.Paciente;
import java.util.List;

public class MainTeste {
    public static void main(String[] args) {

        PacienteDao dao = new PacienteDao();

        // ---------------------------------------------
        // PARTE 1: CADASTRAR (Inserir no Banco)
        // ---------------------------------------------
        System.out.println("--- Iniciando Cadastro ---");

        Paciente p1 = new Paciente();
        p1.setNome("João da Silva");
        p1.setCpf("111.222.333-44");
        p1.setTelefone("(11) 99999-9999");
        p1.setData_nascimento("01/01/1980");
        p1.setEndereco("Rua Teste, 123");
        p1.setHistoricoGeral("Paciente teste inserido via Java");

        dao.cadastrar(p1); // <--- Aqui grava no banco!

        System.out.println("--- Cadastro Finalizado ---");
        System.out.println(""); // Pula uma linha

        // ---------------------------------------------
        // PARTE 2: LISTAR (Ler do Banco)
        // ---------------------------------------------
        System.out.println("--- Lista de Pacientes no Banco ---");

        List<Paciente> lista = dao.listarTodos();

        // Se a lista estiver vazia, avisa
        if (lista.isEmpty()) {
            System.out.println("Nenhum paciente encontrado (O Banco está vazio).");
        } else {
            // Se tiver gente, imprime um por um
            for (Paciente p : lista) {
                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() + " | CPF: " + p.getCpf());
            }
        }
    }
}
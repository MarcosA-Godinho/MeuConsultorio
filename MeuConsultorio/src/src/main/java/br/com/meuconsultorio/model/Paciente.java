package br.com.meuconsultorio.model;

public class Paciente {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String data_nascimento;
    private String endereco;
    private String historicoGeral; //ARMAZENA O PRONTURAIO GERAL DO PACIENTE

    public Paciente() {} //CONSTRUTOR VAZIO (BOAS PRATICAS)

    //CONSTRUTOR PARA FACILITAR A CRIAÇÃO
    public Paciente(String nome, String cpf, String telefone){
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHistoricoGeral() {
        return historicoGeral;
    }

    public void setHistoricoGeral(String historicoGeral) {
        this.historicoGeral = historicoGeral;
    }

    @Override
    public String toString() {
        return "Paciente: " + nome + " (CPF: " + cpf + ")";
    }



}

package br.com.meuconsultorio.model;

public class Convenio {
    private Long id;
    private String nome; // Ex: "Unimed", "Particular"
    private double valor; // Ex: 100.0

    public Convenio() {}

    public Convenio(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    @Override
    public String toString() {
        return nome; // Para aparecer bonitinho no ComboBox depois
    }
}
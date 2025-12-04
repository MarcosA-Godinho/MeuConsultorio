package br.com.meuconsultorio.model;

public class Sessao {
    private Long id;
    private Long idPaciente; // O "Link" com o paciente
    private String nomePaciente; // Apenas para facilitar a exibição na tela (não salvamos no banco)
    private String data; // Formato: dd/MM/yyyy
    private String hora; // Formato: HH:mm
    private String status; // Ex: "Agendado", "Realizado", "Cancelado"
    private String observacao; // O que aconteceu NESTA sessão específica

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
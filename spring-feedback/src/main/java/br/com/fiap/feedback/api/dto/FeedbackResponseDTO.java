package br.com.fiap.feedback.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class FeedbackResponseDTO {

    @Schema(description = "Identificador único do comentário.")
    private Long id;
    @Schema(description = "Descrição da disciplina avaliada", example = "Banco de dados")
    private String disciplina;
    @Schema(description = "Comentário do aluno.", example = "Aula muito completa, boa didática do professor.")
    private String descricao;
    @Schema(description = "Nota enviada.", example = "10")
    private Integer nota;
    @Schema(description = "Classificação quanto à prioridade da avaliação.", example = "ALTA, BAIXA ou MÉDIA")
    private String urgencia;
    @Schema(description = "Aluno que comentou.", example = "José Nilvo Nivaldo")
    private String aluno;
    @Schema(description = "Professor avaliado.", example = "João Pedro da Cunha")
    private String professor;
    @Schema(description = "Data do envio do comentário", example = "07/01/2025")
    private LocalDateTime dataEnvio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getAluno() {
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}

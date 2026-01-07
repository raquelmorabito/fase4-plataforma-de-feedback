package br.com.fiap.feedback.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeedbackRequestDTO {

    @NotBlank(message = "Descreva sua avaliação.")
    @Schema(description = "Comentário da avaliação do aluno.", example = "Aula muito completa, boa didática do professor.", nullable = false)
    private String descricao;

    @NotBlank(message = "Descreva qual disciplina está sendo avaliada.")
    @Schema(description = "Descrição da disciplina avaliada", example = "Banco de dados", nullable = false)
    private String disciplina;

    @Schema(description = "ID do usuário professor da disciplina", example = "9", nullable = false)
    @NotNull(message = "Informe o ID do professor avaliado.")
    private Long professor;

    @Schema(description = "Nota de 0 a 10", example = "10", nullable = false)
    @NotNull(message = "Forneça uma nota de 0 a 10.")
    @Min(0)
    @Max(10)
    private Integer nota;

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

    public @NotBlank(message = "Descreva qual disciplina está sendo avaliada.") String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(@NotBlank(message = "Descreva qual disciplina está sendo avaliada.") String disciplina) {
        this.disciplina = disciplina;
    }

    public @NotNull(message = "Informe o ID do professor avaliado.") Long getProfessor() {
        return professor;
    }

    public void setProfessor(@NotNull(message = "Informe o ID do professor avaliado.") Long professor) {
        this.professor = professor;
    }
}
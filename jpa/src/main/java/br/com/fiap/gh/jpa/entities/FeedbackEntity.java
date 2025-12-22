package br.com.fiap.gh.jpa.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class FeedbackEntity implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String disciplina;
    private String descricao;
    private Integer nota;
    private String urgencia; //preenchido auto conforme nota
    private LocalDateTime dataEnvio;

    @ManyToOne
    @JoinColumn(name = "usuario_aluno")
    private UsuarioEntity aluno;// preenchido auto com usuario logado

    @ManyToOne
    @JoinColumn(name = "usuario_professor")
    private UsuarioEntity professor; //enviar id no postman

    public FeedbackEntity() {
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public UsuarioEntity getAluno() {
        return aluno;
    }

    public void setAluno(UsuarioEntity aluno) {
        this.aluno = aluno;
    }

    public UsuarioEntity getProfessor() {
        return professor;
    }

    public void setProfessor(UsuarioEntity professor) {
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

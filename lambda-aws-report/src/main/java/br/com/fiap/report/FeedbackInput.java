package br.com.fiap.report;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FeedbackInput implements Serializable {

    public Long feedbackId;

    public String curso;
    public String aluno;
    public Integer notaAvaliacao;
    public LocalDateTime dataAvalicacao;

    public String comentario;

    public String emailDestinatario;

    public FeedbackInput() {
    }

    public FeedbackInput(Long feedbackId, String curso, String aluno, Integer notaAvaliacao, LocalDateTime dataAvalicacao, String comentario, String emailDestinatario) {
        this.feedbackId = feedbackId;
        this.curso = curso;
        this.aluno = aluno;
        this.notaAvaliacao = notaAvaliacao;
        this.dataAvalicacao = dataAvalicacao;
        this.comentario = comentario;
        this.emailDestinatario = emailDestinatario;
    }
}

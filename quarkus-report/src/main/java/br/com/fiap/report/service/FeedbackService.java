package br.com.fiap.report.service;

import br.com.fiap.report.repository.FeedbackRepository;
import br.com.fiap.report.dto.MailDTO;
import br.com.fiap.report.dto.ReportDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;

import java.util.List;

@ApplicationScoped
public class FeedbackService {

    @Inject
    FeedbackRepository repository;

    @Inject
    EmailService emailService;

    @Inject
    GeminiService geminiService;

    public void gerarRelatorioEEnviarEmail() {

        var feedbacks = repository.buscarFeedbacks();

        for (var reportDTO : feedbacks) {
            try {

                List<String> comentarios = repository.buscarTodosComentarios(reportDTO.disciplina(), reportDTO.professor_id());
                String resumo = geminiService.resumirComentarios(comentarios);

               var mailDTO = new MailDTO("Avaliação da disciplina - "+reportDTO.disciplina(),
                    montarCorpoEmail(reportDTO, resumo), reportDTO.email());

                emailService.send(mailDTO);

            } catch (MessagingException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }

    }

    private String montarCorpoEmail(ReportDTO reportDTO, String resumo) {
        String corpo = "Prezado(a) "+reportDTO.nome_professor()+", <br/><br/>";

        corpo += "Sua disciplina de <b>"+reportDTO.disciplina()+"</b> recebeu uma nota média de <b>"+reportDTO.media_avaliacoes()+"</b> nas avaliações!<br/>";

        corpo += "Resumo dos comentários: <b>"+resumo+"</b>.<br/>";

        corpo += "Em breve enviaremos um resumo com os principais comentários.";

        return corpo;
    }
}


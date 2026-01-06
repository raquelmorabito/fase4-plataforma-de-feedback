package br.com.fiap.report;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;

@ApplicationScoped
public class FeedbackService {

    @Inject
    FeedbackRepository repository;

    @Inject
    EmailService emailService;

    public void gerarRelatorioEEnviarEmail() {

        var feedbacks = repository.buscarFeedbacks();

        for (var reportDTO : feedbacks) {
            try {

               var mailDTO = new MailDTO("Avaliação discplina "+reportDTO.disciplina(),
                    montarCorpoEmail(reportDTO),reportDTO.email());

                emailService.send(mailDTO);

            } catch (MessagingException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }

    }

    private String montarCorpoEmail(ReportDTO reportDTO) {
        String corpo = "Prezado(a) "+reportDTO.nome_professor()+", <br/><br/>";

        corpo += "Sua disciplina de <b>"+reportDTO.disciplina()+"</b> recebeu uma nota média de <b>"+reportDTO.media_avaliacoes()+"</b> nas avaliações!<br/>";


        corpo += "Em breve enviaremos um resumo com os principais comentários.";

        return corpo;
    }
}


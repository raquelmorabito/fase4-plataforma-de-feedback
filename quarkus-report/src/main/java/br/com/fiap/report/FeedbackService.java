package br.com.fiap.report;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FeedbackService {

    @Inject
    FeedbackRepository repository;

    @Inject
    EmailService emailService;

    public void gerarRelatorioEEnviarEmail() {

        var feedbacks = repository.buscarFeedbacks();

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório de Feedback\n\n");

//        for (var f : feedbacks) {
//            relatorio.append("Nota: ")
//                    .append(f.)
//                    .append(" - ")
//                    .append(f.comentario)
//                    .append("\n");
//        }

//        emailService.send(
//                Mail.withText(
//                        "destinatario@exemplo.com",
//                        "Relatório de Feedback",
//                        relatorio.toString()
//                )
//        );
    }
}


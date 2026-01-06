package br.com.fiap.report;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ScheduledFeedbackJob {

    @Inject
    FeedbackService feedbackService;

    public void gerarRelatorio() {
        feedbackService.gerarRelatorioEEnviarEmail();
    }
}

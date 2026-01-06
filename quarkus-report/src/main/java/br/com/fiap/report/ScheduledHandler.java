package br.com.fiap.report;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;
import jakarta.inject.Inject;

public class ScheduledHandler
        implements RequestHandler<Map<String, Object>, Void> {

    @Inject
    FeedbackService feedbackService;

    @Override
    public Void handleRequest(Map<String, Object> event, Context context) {
        feedbackService.gerarRelatorioEEnviarEmail();
        return null;
    }
}

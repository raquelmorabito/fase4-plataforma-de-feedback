package br.com.fiap.report;
import br.com.fiap.report.service.FeedbackService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/send")
public class LocalRunner {

    @Inject
    FeedbackService feedbackService;

    @GET
    public String test() {
        feedbackService.gerarRelatorioEEnviarEmail();
        return "OK";
    }
}


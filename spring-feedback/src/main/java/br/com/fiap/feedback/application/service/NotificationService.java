package br.com.fiap.feedback.application.service;

import br.com.fiap.feedback.api.dto.FeedbackLambdaInput;
import br.com.fiap.gh.jpa.entities.FeedbackEntity;
import br.com.fiap.gh.security.JwtService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private final AuthenticationService authenticationService;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String servelessFunctionNotification
            = "https://gfxs3x7yytoc3zysfgoo7rhdhy0fpczu.lambda-url.us-east-1.on.aws/";
//            System.getenv( "AWS_LAMBDA_NOTIFICATION_URL");

    public NotificationService( AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void sendNewNotificationWhenUrgent(FeedbackEntity feedback) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // gera ou recupera o token já autenticado
        String username = authenticationService.getAuthUsername();
        String token = authenticationService.generateNewToken(username);
        headers.setBearerAuth(token);

        var input = FeedbackLambdaInput.create(feedback);

        HttpEntity<FeedbackLambdaInput> requestContent = new HttpEntity<>(input, headers);

        try {

            var retorno =  restTemplate.postForObject(
                    servelessFunctionNotification,
                    requestContent,
                    String.class
            );

        } catch (RestClientException ex) {
            // Loga e segue o fluxo — notificação não pode quebrar o sistema
            System.err.println("Erro ao chamar Lambda de notificação "+ ex.getMessage());
        }
    }
}


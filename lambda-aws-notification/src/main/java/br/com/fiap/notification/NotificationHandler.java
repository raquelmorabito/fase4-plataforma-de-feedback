package br.com.fiap.notification;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.security.PublicKey;
import java.util.Map;

/**
 * https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html
 *
 */
public class NotificationHandler {

    private static final PublicKey PUBLIC_KEY = PemUtils.loadPublicKey("public-key.pem");

    private final ObjectMapper mapper = new ObjectMapper();
    private final EmailService emailService = new EmailService();

    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent event,
            Context context) {

        return checkAuthorization(event, context);
    }

    private APIGatewayProxyResponseEvent processJsonBody(APIGatewayProxyRequestEvent event,
                                                         Context context) {

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            FeedbackInput inputDTO =
                    mapper.readValue(event.getBody(), FeedbackInput.class);

            System.out.println("chegou - " + inputDTO.emailDestinatario);

            emailService.send(inputDTO);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("OK");

        } catch (Exception e) {

            e.printStackTrace();

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Erro");
        }
    }

    private String getAuthHeader(Map<String, String> headers) {
        if (headers == null) return null;

        return headers.getOrDefault(
                "Authorization",
                headers.get("authorization")
        );
    }

    private APIGatewayProxyResponseEvent unauthorized(String msg) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(401)
                .withBody(msg);
    }

    public APIGatewayProxyResponseEvent checkAuthorization(
            APIGatewayProxyRequestEvent event,
            Context context) {

        String auth = getAuthHeader(event.getHeaders());

        if (auth == null || !auth.startsWith("Bearer ")) {
            return unauthorized("Token ausente ou inválido!");
        }

        String token = auth.substring(7);

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(PUBLIC_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Usuario autorizado > " +claims.getSubject());
            // token válido

            processJsonBody(event, context);
            // aqui você pode usar claims.getSubject(), etc.

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("POST autorizado");

        } catch (JwtException e) {
            return unauthorized("Token inválido");
        }
    }
}

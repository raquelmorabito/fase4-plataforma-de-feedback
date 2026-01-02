package br.com.fiap.report;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private static final String environment_EMAIL_USER_NAME = System.getenv("EMAIL_USER_NAME");
    private static final String environment_EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");

    public void send(FeedbackInput dto) throws MessagingException {

        System.out.println("username encontrado: "+environment_EMAIL_USER_NAME);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                environment_EMAIL_USER_NAME,
                                environment_EMAIL_PASSWORD
                        );
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("notificacao@feedback.com"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(dto.emailDestinatario)
        );
        message.setSubject("Feedback urgente - Lambda Puro");
        message.setText(
                "Curso: " + dto.curso +
                        "\nAvaliação: " + dto.notaAvaliacao +
                        "\nComentário: " + dto.comentario
        );

        Transport.send(message);
    }
}

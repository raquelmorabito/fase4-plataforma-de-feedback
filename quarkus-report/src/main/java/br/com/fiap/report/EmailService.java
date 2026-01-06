package br.com.fiap.report;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;

@ApplicationScoped
public class EmailService {

    @Inject
    @ConfigProperty(name = "email.user.name")
    String emailUserName;

    @Inject
    @ConfigProperty(name = "email.user.password")
    String emailPassword;

    public void send(MailDTO dto) throws MessagingException {

//        System.out.println("EMAIL USUARIO ENCONTRADO: "+ emailUserName + "<senha>: "+ emailPassword);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                emailUserName,
                                emailPassword
                        );
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("notificacao@feedback.com"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(dto.emailDestinatario())
        );
        message.setSubject(dto.assunto());
        message.setContent(dto.corpoHTML(), "text/html; charset=utf-8");

        Transport.send(message);
    }
}

package bumh3r.authentication.otp;

import bumh3r.model.Usuario;
import bumh3r.model.other.DateFull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSender {

    private final String emailUsername;
    private final String emailPassword;
    private final Properties properties;

    public EmailSender(String emailUsername, String emailPassword) {
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;

        this.properties = new Properties();
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.starttls.enable", "true");
        this.properties.put("mail.smtp.host", "smtp.gmail.com");
        this.properties.put("mail.smtp.port", "587");
    }


    private Session createSession() {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });
    }

    private Message createMessage(Session session, String recipientEmail, String subject, String contentHtml) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailUsername));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail)
        );
        message.setSubject(subject);
        message.setContent(contentHtml, "text/html; charset=utf-8");
        return message;
    }

    public void sendOtpEmail(String recipientEmail, String otp) throws MessagingException {
        Session session = createSession();
        String contentHtml = getHtmlFromResources(TypeTemplate.OTP.getPath());

        if (contentHtml == null) {
            throw new MessagingException("Error al enviar el código de verificación");
        }

        for (int i = 0; i < otp.length(); i++) {
            contentHtml = contentHtml.replace("{{CODE" + (i + 1) + "}}", String.valueOf(otp.charAt(i)));
        }

        Message message = createMessage(session, recipientEmail, TypeTemplate.OTP.getTitle(), contentHtml);
        Transport.send(message);
    }

    public void sendRegisterSuccessEmail(String email_cliente, Usuario user) throws MessagingException {
        Session session = createSession();
        String contentHtml = getHtmlFromResources(TypeTemplate.REGISTER_SUCCESS.getPath());

        if (contentHtml == null) {
            throw new MessagingException("Error al enviar el correo de registro");
        }

        Map<String, String> replacements = Map.of(
                "{{USER}}",user.getUsername(),
                "{{NOMBRE}}", user.getFirstname(),
                "{{APELLIDOS}}", user.getLastname(),
                "{{FECHA}}", DateFull.getDateFullWithTime(LocalDateTime.now())
        );

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            contentHtml = contentHtml.replace(entry.getKey(), entry.getValue());
        }

        Message message = createMessage(session, email_cliente, TypeTemplate.REGISTER_SUCCESS.getTitle(), contentHtml);
        Transport.send(message);
    }

    private String getHtmlFromResources(String resourcePath) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo HTML: " + e.getMessage());
            return null;
        }
        return content.toString();
    }

}
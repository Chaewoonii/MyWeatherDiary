package com.cnu.diary.myweatherdiary.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
public class GmailSmtp {

    private final String HOST;
    private final String PORT;
    private final String SENDER;
    private final String SECRET;

    public GmailSmtp(GmailSmtpProperties properties){
        this.HOST = properties.getHost();
        this.PORT = properties.getPort();
        this.SENDER = properties.getAccount();
        this.SECRET = properties.getSecret();
    }

    public void sendMail(String recipient, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER, SECRET);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
//            log.info("Email sent successfully. to: {}", recipient);
        } catch (MessagingException e) {
//            log.info("Failed to send email. to: {}", recipient);
            e.printStackTrace();
        }
    }
}

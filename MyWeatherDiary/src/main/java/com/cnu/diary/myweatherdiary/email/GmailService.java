package com.cnu.diary.myweatherdiary.email;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class GmailService {

    @Autowired
    private static Gmail gmail;

    public static MimeMessage createMail(String toEmailAddr, String fromEmailAddr, String subject, String bodyText) throws MessagingException {
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(fromEmailAddr));
        email.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddr));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static com.google.api.services.gmail.model.Message createMesageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static com.google.api.services.gmail.model.Message sendEmail(String fromEmailAddress,
                                                                        String toEmailAddress,
                                                                        String subject,
                                                                        String bodyText)
            throws MessagingException, IOException {
        MimeMessage emailContent = createMail(fromEmailAddress, toEmailAddress, subject, bodyText);
        com.google.api.services.gmail.model.Message message = createMesageWithEmail(emailContent);

        try {
            // Create send message
            message = gmail.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message;
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
        return null;
    }

}

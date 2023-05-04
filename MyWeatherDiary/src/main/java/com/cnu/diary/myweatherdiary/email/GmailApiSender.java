package com.cnu.diary.myweatherdiary.email;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;


public class GmailApiSender {

    private final Gmail gmail;

    private static final String SENDER = "mwd2023cnu@gmail.com";

    private static final String SUBJECT = "Hello! It's MyWeatherDiary";

    private static final String SITE = "http://www.myweatherdiary.site";

    public GmailApiSender(GmailConfig gmailConfig) throws GeneralSecurityException, IOException {
        this.gmail = gmailConfig.gmailBean();
    }

    public void printLabel(String user) throws IOException {
        ListLabelsResponse listResponse = gmail.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
    }

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

    public com.google.api.services.gmail.model.Message sendEmail(String toEmailAddress,
                                                                        String fromEmailAddress,
                                                                        String subject,
                                                                        String bodyText)
            throws MessagingException, IOException {
        MimeMessage emailContent = createMail(toEmailAddress, fromEmailAddress, subject, bodyText);
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

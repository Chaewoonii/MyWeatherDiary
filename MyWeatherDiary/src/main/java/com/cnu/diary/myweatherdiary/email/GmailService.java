package com.cnu.diary.myweatherdiary.email;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

//@Service
public class GmailService {

    private static Gmail gmail = null;

    private static final String SENDER = "mwd2023cnu@gmail.com";

    private static final String SUBJECT = "Hello! It's MyWeatherDiary";

    private static final String SITE = "http://www.myweatherdiary.site";

    public GmailService() throws GeneralSecurityException, IOException {
        this.gmail = GmailConfig.gmailBean();
    }

    public void sendEnterKey(String receiver, String diaryTitle, String enterKey) throws MessagingException, IOException {
        String bodyText = "안녕하세요, MyWeatherDiary 일기장을 등록해주셔서 감사합니다.\n" +
                "당신의 일기 <" + diaryTitle + ">의 비밀 Key는 아래와 같습니다\n"
                + enterKey + "\n" +
                "로그인 시, 위의 Key 를 입력해주세요. 또한, 비밀 Key를 잃어버릴 경우, 복구가 불가능하니 반드시 어딘가에 저장해 주세요.\n" +
                "MyWeatherDiary는 당신만의 동굴입니다. 당신의 세상을 기록해보세요!\n\n"
                +SITE;

        sendEmail(receiver, SENDER, SUBJECT, bodyText);
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

    public static com.google.api.services.gmail.model.Message sendEmail(String toEmailAddress,
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

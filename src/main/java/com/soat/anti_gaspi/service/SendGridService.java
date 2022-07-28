package com.soat.anti_gaspi.service;

import com.sendgrid.*;
import com.soat.anti_gaspi.model.NotificationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Qualifier("SendGridService")
public class SendGridService implements EmailService {
    @Value("${senGrid.send-grid-api-key}")
    String sendGridApiKey;

    @Override
    public void sendEmail(String subject, String beneficiaire, String body) {
        Email from = new Email("no-reply@anti-gaspi.fr");
        Email to = new Email("no-reply@anti-gaspi.fr");
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

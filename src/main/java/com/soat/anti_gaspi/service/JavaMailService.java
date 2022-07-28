package com.soat.anti_gaspi.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
@Qualifier("JavaMailService")
public class JavaMailService implements EmailService{
    @Override
    public void sendEmail(String subject, String beneficiaire, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            props.put("mail.smtp.port", "" + 9999);
            Session session = Session.getInstance(props, null);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("no-reply@anti-gaspi.fr"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(beneficiaire));
            msg.setSubject(subject);
            msg.setContent(body, "text/plain; charset=UTF-8");
            Transport.send(msg);
        } catch (MessagingException e) {
//            throw new NotificationException(e);
            e.printStackTrace();
        }
    }

}

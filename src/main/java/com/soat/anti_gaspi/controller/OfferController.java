package com.soat.anti_gaspi.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.soat.anti_gaspi.model.NotificationException;
import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.repository.OfferRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(OfferController.PATH)
public class OfferController {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String PATH = "/annonces";
    private final OfferRepository offerRepository;

    public OfferController(OfferRepository offerRepository) {

        this.offerRepository = offerRepository;
    }

    @PostMapping("")
    public ResponseEntity<UUID> create(@RequestBody OfferJson offerJson) throws NotificationException {
        Offer offer = new Offer(
              offerJson.company(),
              offerJson.title(),
              offerJson.description(),
              offerJson.email(),
              offerJson.address(),
              LocalDate.parse(offerJson.availabilityDate(), dateFormatter),
              LocalDate.parse(offerJson.expirationDate(), dateFormatter));
        offer.setId(UUID.randomUUID());
        var saved = offerRepository.save(offer);

        // TODO: send email to the offer creator
        String emailBody = String.format("%s %s %s %s %s", offer.getDescription(), offer.getAddress(), offer.getCompany(), offer.getAvailabilityDate(), offer.getExpirationDate());
        envoiMail(offer.getTitle(), offer.getEmail(), emailBody);

        return new ResponseEntity<>(saved.getId(), HttpStatus.CREATED);
    }

    private void envoiMail(String subject, String beneficiaire, String body) throws NotificationException {
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
            throw new NotificationException(e);
        }
    }
}

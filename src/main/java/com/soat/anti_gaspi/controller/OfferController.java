package com.soat.anti_gaspi.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.soat.anti_gaspi.model.NotificationException;
import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.model.Status;
import com.soat.anti_gaspi.repository.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(OfferController.PATH)
public class OfferController {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String PATH = "/api/offers";
    private final OfferRepository offerRepository;

    public OfferController(OfferRepository offerRepository) {

        this.offerRepository = offerRepository;
    }

    @PostMapping("")
    public ResponseEntity<UUID> create(@RequestBody OfferJson offerJson) throws NotificationException {
        Offer offer = new Offer(
                offerJson.companyName(),
                offerJson.title(),
                offerJson.description(),
                offerJson.email(),
                offerJson.address(),
                LocalDate.parse(offerJson.availabilityDate(), dateFormatter),
                LocalDate.parse(offerJson.expirationDate(), dateFormatter));
        offer.setId(UUID.randomUUID());
        var saved = offerRepository.save(offer);

        String emailBody = String.format("%s %s %s %s %s", offer.getDescription(), offer.getAddress(), offer.getCompanyName(), offer.getAvailabilityDate(), offer.getExpirationDate());
        sendEmail(offer.getTitle(), offer.getEmail(), emailBody);

        return new ResponseEntity<>(saved.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable("id") UUID uuid) {
        Optional<Offer> maybeOffer = offerRepository.findById(uuid);
        AtomicReference<HttpStatus> status = new AtomicReference<>(HttpStatus.NOT_FOUND);
        maybeOffer.ifPresent(offer -> {
            offer.setStatus(Status.PUBLISHED);
            offerRepository.save(offer);
            status.set(HttpStatus.ACCEPTED);
        });
        return new ResponseEntity<>(status.get());
    }

    @GetMapping
    public ResponseEntity<List<OfferSavedJson>> getPublishedOffers() {
        var allOffers = (List<Offer>) offerRepository.findAll();
        var publishedOffers = allOffers.stream()
                .filter(offer -> Status.PUBLISHED.equals(offer.getStatus()))
                .map(offer -> new OfferSavedJson(offer.getId(), offer.getCompanyName(), offer.getTitle(), offer.getDescription(), offer.getEmail(), offer.getAddress(), offer.getAvailabilityDate(), offer.getExpirationDate()))
                .toList();
        return new ResponseEntity<>(publishedOffers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        offerRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void sendEmail(String subject, String beneficiaire, String body) throws NotificationException {
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

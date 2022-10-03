package com.soat.anti_gaspi.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.repository.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UUID> create(@RequestBody OfferToSave offerToSave) {
        Offer offer = new Offer(
                offerToSave.companyName(),
                offerToSave.title(),
                offerToSave.description(),
                offerToSave.email(),
                offerToSave.address(),
                LocalDate.parse(offerToSave.availabilityDate(), dateFormatter),
                LocalDate.parse(offerToSave.expirationDate(), dateFormatter));
        offer.setId(UUID.randomUUID());
        var saved = offerRepository.save(offer);
        return new ResponseEntity<>(saved.getId(), HttpStatus.CREATED);
    }
}

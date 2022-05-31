package com.soat.anti_gaspi.controller;

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
    public static final String PATH = "/annonces";
    private final OfferRepository offerRepository;

    public OfferController(OfferRepository offerRepository) {

        this.offerRepository = offerRepository;
    }

    @PostMapping("")
    public ResponseEntity<UUID> create(@RequestBody Offer offer) {
        offer.setId(UUID.randomUUID());
        var saved = offerRepository.save(offer);
        return new ResponseEntity<>(saved.getId(), HttpStatus.CREATED);
    }
}

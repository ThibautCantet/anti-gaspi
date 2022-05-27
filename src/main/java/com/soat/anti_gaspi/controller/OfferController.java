package com.soat.anti_gaspi.controller;

import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.repository.AnnonceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(OfferController.PATH)
public class OfferController {
    public static final String PATH = "/annonces";
    private final AnnonceRepository offerRepository;

    public OfferController(AnnonceRepository offerRepository) {

        this.offerRepository = offerRepository;
    }

    @PostMapping("")
    public ResponseEntity<Integer> create(@RequestBody Offer offer) {
        var saved = offerRepository.save(offer);
        return created(saved.getId()).build();
    }
}

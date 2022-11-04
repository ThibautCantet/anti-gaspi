package com.soat.anti_gaspi.controller;

import java.util.List;

import com.soat.anti_gaspi.dto.OfferPage;
import com.soat.anti_gaspi.dto.SavedOffer;
import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.model.Status;
import com.soat.anti_gaspi.repository.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(OfferController.PATH)
public class OfferController {
    public static final String PATH = "/api/offers";
    private final OfferRepository offerRepository;

    public OfferController(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @GetMapping
    public ResponseEntity<OfferPage> getPublishedOffers(@RequestParam int pageNumber,
                                                        @RequestParam int pageSize,
                                                        @RequestParam String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortOrder) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, "desc".equals(sortOrder) ? Sort.by(sortBy).descending() : Sort.by(sortBy));

        Page<Offer> allOffers = offerRepository.findAllByStatus(Status.PUBLISHED, pageable);

        List<SavedOffer> savedOffers = allOffers.stream()
                .map(this::toOfferSavedJson)
                .toList();

        var result = new OfferPage(savedOffers, allOffers.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private SavedOffer toOfferSavedJson(Offer offer) {
        return new SavedOffer(offer.getId(),
                offer.getCompanyName(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getEmail(),
                offer.getAddress(),
                offer.getAvailabilityDate(),
                offer.getExpirationDate());
    }

}

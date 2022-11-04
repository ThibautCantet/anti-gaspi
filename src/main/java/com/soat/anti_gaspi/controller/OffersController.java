package com.soat.anti_gaspi.controller;

import java.util.ArrayList;
import java.util.List;

import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.model.User;
import com.soat.anti_gaspi.model.UserSession;
import com.soat.anti_gaspi.repository.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(OffersController.PATH)
public class OffersController {
    public static final String PATH = "/api/offers";
    private final OfferRepository offerRepository;

    public OffersController(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getOffersByUser(@RequestBody User user) {
        List<Offer> offerList = new ArrayList<Offer>();
        User loggedUser = UserSession.getInstance().getLoggedUser();
        boolean isFriend = false;
        if (loggedUser != null) {
            for (User friend : user.getFriends()) {
                if (friend.equals(loggedUser)) {
                    isFriend = true;
                    break;
                }
            }
            if (isFriend) {
                offerList = offerRepository.findOffersByUser(user);
            }
            return new ResponseEntity<>(offerList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

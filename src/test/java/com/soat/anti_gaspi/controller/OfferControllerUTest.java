package com.soat.anti_gaspi.controller;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import com.soat.anti_gaspi.model.NotificationException;
import com.soat.anti_gaspi.repository.ContactRepository;
import com.soat.anti_gaspi.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class OfferControllerUTest {

    private OfferController offerController;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private ContactRepository contactRepository;
    private final Clock clock = Clock.fixed(LocalDate.parse("2022-07-25").atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));;

    @BeforeEach
    void setUp() {
        offerController = new OfferController(offerRepository, contactRepository, clock);
    }

    @Test
    void create_should_return_when_expiration_date_is_in_the_past() throws NotificationException {
        // given
        OfferToSave offerToSave = new OfferToSave("SOAT",
                "3 vieux ordinateurs",
                "3 ordinateurs sous Windows 10 en bon état",
                "revendeur@donner.fr",
                "20 rue des frigos, 75013 Paris",
                "2022-05-31",
                "2022-06-30");

        // when
        ResponseEntity<UUID> result = offerController.create(offerToSave);

        // then
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}

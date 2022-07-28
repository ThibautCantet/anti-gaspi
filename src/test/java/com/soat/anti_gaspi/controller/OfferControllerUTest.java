package com.soat.anti_gaspi.controller;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import com.dumbster.smtp.SimpleSmtpServer;
import com.soat.anti_gaspi.model.NotificationException;
import com.soat.anti_gaspi.repository.ContactRepository;
import com.soat.anti_gaspi.repository.OfferRepository;
import com.soat.anti_gaspi.service.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Mock
    private EmailService smailService;

    private final Clock clock = Clock.fixed(LocalDate.parse("2022-07-28").atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));;


    public static final int SMTP_PORT = 9999;
    private SimpleSmtpServer mailServer;

    @BeforeEach
    void setUp() throws IOException {
        offerController = new OfferController(smailService, offerRepository, contactRepository, clock);
        mailServer = SimpleSmtpServer.start(SMTP_PORT);
    }

    @AfterEach
    void tearDown() {
        mailServer.stop();
    }

    @Nested
    class Create {
        @Test
        void should_return_bad_request_when_availability_date_is_in_the_past() throws NotificationException {
            // given
            OfferToSave offerToSave = new OfferToSave("SOAT",
                    "3 vieux ordinateurs",
                    "3 ordinateurs sous Windows 10 en bon état",
                    "revendeur@donner.fr",
                    "20 rue des frigos, 75013 Paris",
                    "2022-05-31",
                    "2022-09-30");

            // when
            ResponseEntity<UUID> result = offerController.create(offerToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }

        @Test
        void should_return_bad_request_when_expiration_date_is_in_the_past() throws NotificationException {
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

        @Test
        void should_return_bad_request_when_invalid_email() throws NotificationException {
            // given
            OfferToSave offerToSave = new OfferToSave("SOAT",
                    "3 vieux ordinateurs",
                    "3 ordinateurs sous Windows 10 en bon état",
                    "revendeur@invalid-email",
                    "20 rue des frigos, 75013 Paris",
                    "2022-05-31",
                    "2022-08-30");

            // when
            ResponseEntity<UUID> result = offerController.create(offerToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }


        @Test
        void should_return_bad_request_when_empty_Company_name() throws NotificationException {
            // given
            OfferToSave offerToSave = new OfferToSave("",
                    "3 vieux ordinateurs",
                    "3 ordinateurs sous Windows 10 en bon état",
                    "revendeur@soat.fr",
                    "20 rue des frigos, 75013 Paris",
                    "2022-05-31",
                    "2022-08-30");

            // when
            ResponseEntity<UUID> result = offerController.create(offerToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }

        @Test
        void should_return_bad_request_when_empty_title() throws NotificationException {
            // given
            OfferToSave offerToSave = new OfferToSave("SOAT",
                    "",
                    "3 ordinateurs sous Windows 10 en bon état",
                    "revendeur@soat.fr",
                    "20 rue des frigos, 75013 Paris",
                    "2022-05-31",
                    "2022-08-30");

            // when
            ResponseEntity<UUID> result = offerController.create(offerToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }

        @Test
        void should_return_bad_request_when_empty_description() throws NotificationException {
            // given
            OfferToSave offerToSave = new OfferToSave("SOAT",
                    "3 vieux ordinateurs",
                    "",
                    "revendeur@soat.fr",
                    "20 rue des frigos, 75013 Paris",
                    "2022-05-31",
                    "2022-08-30");

            // when
            ResponseEntity<UUID> result = offerController.create(offerToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }

        @Test
        void should_return_bad_request_when_empty_address() throws NotificationException {
            // given
            OfferToSave offerToSave = new OfferToSave("SOAT",
                    "3 vieux ordinateurs",
                    "3 ordinateurs sous Windows 10 en bon état",
                    "revendeur@soat.fr",
                    "",
                    "2022-05-31",
                    "2022-08-30");

            // when
            ResponseEntity<UUID> result = offerController.create(offerToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }
    }

    @Nested
    class CreateContact{
        @Test
        void should_return_bad_request_when_empty_firstname() throws NotificationException {
            // given
            ContactToSave contactToSave = new ContactToSave("Dupond", "", "0712345678", "contact@soat.fr", "lorem ipsum", "9c1845ea-a7be-4848-aba4-66ba33fd6d39");

            // when
            ResponseEntity<UUID> result = offerController.createContact(UUID.fromString("9c1845ea-a7be-4848-aba4-66ba33fd6d40"), contactToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }

        @Test
        void should_return_bad_request_when_empty_lastname() throws NotificationException {
            // given
            ContactToSave contactToSave = new ContactToSave("", "Martin", "0712345678", "contact@soat.fr", "lorem ipsum", "9c1845ea-a7be-4848-aba4-66ba33fd6d39");

            // when
            ResponseEntity<UUID> result = offerController.createContact(UUID.fromString("9c1845ea-a7be-4848-aba4-66ba33fd6d40"), contactToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }
        @Test
        void should_return_bad_request_when_invalid_email() throws NotificationException {
            // given
            ContactToSave contactToSave = new ContactToSave("Dupond", "Martin", "0712345678", "revendeur@invalid-email", "lorem ipsum", "9c1845ea-a7be-4848-aba4-66ba33fd6d39");

            // when
            ResponseEntity<UUID> result = offerController.createContact(UUID.fromString("9c1845ea-a7be-4848-aba4-66ba33fd6d40"), contactToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }

        @Test
        void should_return_bad_request_when_invalid_Num_Phone() throws NotificationException {
            // given
            ContactToSave contactToSave = new ContactToSave("Dupond", "Martin", "=712345678", "revendeur@invalid-email", "lorem ipsum", "9c1845ea-a7be-4848-aba4-66ba33fd6d39");

            // when
            ResponseEntity<UUID> result = offerController.createContact(UUID.fromString("9c1845ea-a7be-4848-aba4-66ba33fd6d40"), contactToSave);

            // then
            assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
        }

    }

}

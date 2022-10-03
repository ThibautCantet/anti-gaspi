package com.soat.anti_gaspi;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soat.ATest;
import com.soat.anti_gaspi.controller.OfferController;
import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.model.Status;
import com.soat.anti_gaspi.repository.OfferRepository;
import io.cucumber.java.Before;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext
@CucumberContextConfiguration
@ActiveProfiles("AcceptanceTest")
public class PublicationAnnonceATest extends ATest {

    @Autowired
    private OfferRepository offerRepository;

    private String companyName;
    private String title;
    private String description;
    private String email;
    private String address;
    private LocalDate availabilityDate;
    private LocalDate expirationDate;
    private Offer offerToSave;

    @Before
    @Override
    public void setUp() throws IOException {
        initIntegrationTest();
        initPath();
    }

    @Override
    protected void initPath() {
        RestAssured.basePath = OfferController.PATH;
    }

    @Etantdonné("l'entreprise {string}")
    public void lEntreprise(String company) {
        this.companyName = company;
    }

    @Etantdonné("le titre {string}")
    public void leTitre(String title) {
        this.title = title;
    }

    @Et("la description {string}")
    public void laDescription(String description) {
        this.description = description;
    }

    @Et("l'email de contact {string}")
    public void lEmailDeContact(String email) {
        this.email = email;
    }

    @Et("l'adresse {string}")
    public void lAdresse(String address) {
        this.address = address;
    }

    @Et("la date de disponibilité {string}")
    public void laDateDeDisponibilité(String availability) {
        this.availabilityDate = LocalDate.parse(availability);
    }

    @Et("la date d'expiration le {string}")
    public void laDateDExpirationLe(String expiration) {
        this.expirationDate = LocalDate.parse(expiration);
    }

    @Quand("on tente une publication d’une annonce")
    public void onTenteUnePublicationDUneAnnonce() throws JsonProcessingException {
        offerToSave = new Offer(
                companyName,
                title,
                description,
                email,
                address,
                availabilityDate,
                expirationDate
        );

        String body = objectMapper.writeValueAsString(offerToSave);
        //@formatter:off
        response = given()
                .log().all()
                .header("Content-Type", ContentType.JSON)
                .body(body)
                .when()
                .post("/");
        //@formatter:on
    }

    @Alors("la publication est enregistrée et un statut est {string}")
    public void laPublicationEstEnregistréeEtUnStatutEst(String statusValue) {
        Status status = Status.from(statusValue);
        UUID id = response.then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(UUID.class);

        var savedOffer = offerRepository.findById(id).orElse(null);
        assertThat(savedOffer).isNotNull();
        assertThat(savedOffer).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(this.offerToSave);
        assertThat(savedOffer.getStatus()).isEqualTo(status);
    }

}

package com.soat.anti_gaspi;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.soat.ATest;
import com.soat.anti_gaspi.controller.OfferController;
import com.soat.anti_gaspi.controller.OfferJson;
import com.soat.anti_gaspi.model.Offer;
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
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.UUID;

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

   private static Logger LOGGER = LoggerFactory.getLogger(PublicationAnnonceATest.class);

   public static final int STMP_PORT = 9999;
   @Autowired
   private OfferRepository offerRepository;

   private SimpleSmtpServer mailServer;

   private String company;
   private String title;
   private String description;
   private String email;
   private String address;
   private LocalDate availabilityDate;
   private LocalDate expirationDate;
   private Offer offerToSave;

   private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

   @Before
   @Override
   public void setUp() {
      initIntegrationTest();
      mailServer = SimpleSmtpServer.start(STMP_PORT);
   }

   @After
   public void tearDown() throws Exception {
      mailServer.stop();
   }

   @Override
   protected void initPath() {
      RestAssured.basePath = OfferController.PATH;
   }

   @Etantdonné("l'entreprise {string}")
   public void lEntreprise(String company) {
      this.company = company;
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
            company,
            title,
            description,
            email,
            address,
            availabilityDate,
            expirationDate
      );

      String body = objectMapper.writeValueAsString(offerToSave);
      initPath();
      //@formatter:off
      response = given()
            .log().all()
            .header("Content-Type", ContentType.JSON)
            .body(body)
            .when()
            .post("/");
      //@formatter:on
   }

   @Alors("la publication est enregistrée")
   public void laPublicationEstEnregistrée() {
      UUID id = response.then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .as(UUID.class);

      var savedOffer = offerRepository.findById(id).orElse(null);
      assertThat(savedOffer).isNotNull();
      assertThat(savedOffer).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(this.offerToSave);
   }

   @Et("un mail de confirmation est envoyé à {string}")
   public void unMailDeConfirmationEstEnvoyéÀ(String email) {
      assertThat(mailServer.getReceivedEmailSize()).isEqualTo(1);
      Iterator<SmtpMessage> emails = mailServer.getReceivedEmail();
      SmtpMessage sentEmail = emails.next();
      String[] destinataires = sentEmail.getHeaderValues("To");
      assertThat(destinataires.length).isEqualTo(1);
      assertThat(destinataires[0]).isEqualTo(email);
      assertThat(sentEmail.getHeaderValue("Subject")).contains(offerToSave.getTitle());
      assertThat(sentEmail.getBody()).contains(offerToSave.getDescription());
      assertThat(sentEmail.getBody()).contains(offerToSave.getCompany());
      assertThat(sentEmail.getBody()).contains(offerToSave.getAddress());
      assertThat(sentEmail.getBody()).contains(offerToSave.getAvailabilityDate().format(dateFormatter));
      assertThat(sentEmail.getBody()).contains(offerToSave.getExpirationDate().format(dateFormatter));
   }
}

package com.soat.anti_gaspi;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.soat.ATest;
import com.soat.anti_gaspi.controller.OfferController;
import com.soat.anti_gaspi.controller.SavedOffer;
import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.model.Status;
import com.soat.anti_gaspi.repository.OfferRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext
@CucumberContextConfiguration
// @ContextConfiguration(classes = { PublicationAnnonceATest.Config.class })
@Import({  PublicationAnnonceATest.Config.class })
@ActiveProfiles("AcceptanceTest")
public class PublicationAnnonceATest extends ATest {

    public static final int STMP_PORT = 9999;
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private Clock clock;

    private SimpleSmtpServer mailServer;

    private UUID id;
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
        mailServer = SimpleSmtpServer.start(STMP_PORT);
        initPath();
    }

    @After
    public void tearDown() {
        mailServer.stop();
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

    @Et("un mail de confirmation est envoyé à {string}")
    public void unMailDeConfirmationEstEnvoyéÀ(String email) throws DecoderException {
        List<SmtpMessage> emails = mailServer.getReceivedEmails();
        assertThat(emails).hasSize(1);
        SmtpMessage sentEmail = emails.get(0);
        System.out.println("sentEmail = " + sentEmail);
        List<String> destinataires = sentEmail.getHeaderValues("To");
        assertThat(destinataires).hasSize(1);
        assertThat(destinataires.get(0)).isEqualTo(email);
        assertThat(sentEmail.getHeaderValue("Subject")).contains(offerToSave.getTitle());
        String body = decodeBody(sentEmail);
        assertThat(body).contains(offerToSave.getDescription());
        assertThat(body).contains(offerToSave.getCompanyName());
        assertThat(body).contains(offerToSave.getAddress());
        assertThat(body).contains(offerToSave.getAvailabilityDate().toString());
        assertThat(body).contains(offerToSave.getExpirationDate().toString());
    }

    private String decodeBody(SmtpMessage email) throws DecoderException {
        String cte = email.getHeaderValue("Content-Transfer-Encoding");
        if ("quoted-printable".equals(cte)) {
            String fixedBody = fixBody(email.getBody());
            QuotedPrintableCodec codec = new QuotedPrintableCodec(StandardCharsets.UTF_8);
            return codec.decode(fixedBody);
        }
        return email.getBody();
    }

    private String fixBody(String body) {
        // lines in an email body are at maximum with 76 chars long
        // the lines that are longer than that can use a CR char ('\r' or char(13)) to soft break the line
        // those characters are escaped with the '=' sign
        // the decoder expect to find a CR char after the '=' found at 76nth positions
        // the mailServer mock library seems to have a bug rendering those lines in the getBody() method
        // let's split the body every 76 chars and glue the part with a CR char
        return Pattern.compile(".{1,76}")
                .matcher(body)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining("\r"));
    }

    @Etantdonné("les annnonces sauvegardées:")
    public void lesAnnnonces(DataTable dataTable) {
        List<Offer> offers = dataTableTransformEntries(dataTable, PublicationAnnonceATest::buildOffer);
        offerRepository.saveAll(offers);
    }

    private static Offer buildOffer(Map<String, String> entry) {
        return new Offer(
                UUID.fromString(entry.get("id")),
                entry.get("entreprise"),
                entry.get("titre"),
                entry.get("description"),
                entry.get("email"),
                entry.get("adresse"),
                LocalDate.parse(entry.get("date de disponibilité")),
                LocalDate.parse(entry.get("date d'expiration")),
                Status.from(entry.get("statut"))
        );

    }

    public static <T> List<T> dataTableTransformEntries(DataTable dataTable, Function<Map<String, String>, T> transformFunction) {
        final List<T> transformResults = new ArrayList<>();
        final List<Map<String, String>> dataTableEntries = dataTable.asMaps(String.class, String.class);
        dataTableEntries.forEach(mapEntry -> {
            transformResults.add(transformFunction.apply(mapEntry));
        });
        return transformResults;
    }

    @Alors("la publication est au statut est {string}")
    public void laPublicationEstAuStatutEst(String statusValue) {
        Status status = Status.from(statusValue);
        response.then()
                .statusCode(HttpStatus.SC_ACCEPTED);

        var updatedOffer = offerRepository.findById(this.id).orElse(null);
        assertThat(updatedOffer).isNotNull();
        assertThat(updatedOffer.getStatus()).isEqualTo(status);
    }

    @Quand("on tente de confirmer l annonce avec l id {string}")
    public void onTenteDeConfirmerLAnnonceAvecLId(String id) {
        this.id = UUID.fromString(id);

        //@formatter:off
        response = given()
                .log().all()
                .header("Content-Type", ContentType.JSON)
                .when()
                .post("/" + id + "/confirm");
        //@formatter:on
    }

    @Quand("on tente d'afficher les annonces")
    public void onTenteDAfficherLesAnnonces() {
        //@formatter:off
        response = given()
                .log().all()
                .header("Content-Type", ContentType.JSON)
                .when()
                .get("/");
        //@formatter:on
    }

    @Alors("la publication les annonces affichées sont:")
    public void laPublicationLesAnnoncesAffichéesSont(DataTable dataTable) {
        List<SavedOffer> savedOffers = dataTableTransformEntries(dataTable, PublicationAnnonceATest::buildOfferSavedJson);
        var detailDtos = response.then().statusCode(HttpStatus.SC_OK).extract()
                .as(SavedOffer[].class);
        assertThat(Arrays.stream(detailDtos).toList())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(savedOffers.toArray(SavedOffer[]::new));
    }

    private static SavedOffer buildOfferSavedJson(Map<String, String> entry) {
        return new SavedOffer(
                UUID.fromString(entry.get("id")),
                entry.get("entreprise"),
                entry.get("titre"),
                entry.get("description"),
                entry.get("email"),
                entry.get("adresse"),
                LocalDate.parse(entry.get("date de disponibilité")),
                LocalDate.parse(entry.get("date d'expiration"))
        );
    }

    @Quand("on tente de supprimer l annonce ave l id {string}")
    public void onTenteDeSupprimerLAnnonceAveLId(String id) {
        //@formatter:off
        response = given()
                .log().all()
                .header("Content-Type", ContentType.JSON)
                .when()
                .delete("/" + id);
        //@formatter:on
    }

    @Alors("l'annonce est bien supprimée")
    public void lAnnonceEstBienSupprimée() {
        response.then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Et("les annonces en base sont:")
    public void lesAnnoncesEnBaseSont(DataTable dataTable) {
        var expectedSavedOffers = dataTableTransformEntries(dataTable, PublicationAnnonceATest::buildOffer);

        List<Offer> savedOffers = (List<Offer>) offerRepository.findAll();

        assertThat(savedOffers)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedSavedOffers.toArray(Offer[]::new));
    }

    @Quand("on tente d'afficher l annonce {string}")
    public void onTenteDAfficherLAnnonce(String id) {
        this.id = UUID.fromString(id);
        //@formatter:off
        response = given()
                .log().all()
                .header("Content-Type", ContentType.JSON)
                .when()
                .get("/" + id);
        //@formatter:on
    }

    @Alors("l annonce affichée contient les informations suivantes {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void lAnnonceAffichéeContientLesInformationsSuivantes(String companyName,
                                                                 String titre,
                                                                 String description,
                                                                 String email,
                                                                 String adresse,
                                                                 String availabilityDate,
                                                                 String expirationDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var expectedOffer = new SavedOffer(
                id,
                companyName,
                titre,
                description,
                email,
                adresse,
                LocalDate.parse(availabilityDate, dateTimeFormatter),
                LocalDate.parse(expirationDate, dateTimeFormatter));

        var offer = response.then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(SavedOffer.class);

        assertThat(expectedOffer).usingRecursiveComparison().isEqualTo(offer);
    }

    @Alors("la publication n'est enregistrée")
    public void laPublicationNEstEnregistrée() {
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Et("la date du jour est le {string}")
    public void laDateDuJourEstLe(String today) {
        // Clock.fixed(LocalDate.parse(today).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
    }

    @Configuration
     class Config {
        @Bean
        public Clock getClock() {
            return Clock.fixed(LocalDate.parse("2023-04-04").atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
        }
    }
}

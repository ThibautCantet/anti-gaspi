package com.soat.anti_gaspi;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import com.soat.ATest;
import com.soat.anti_gaspi.controller.OfferController;
import com.soat.anti_gaspi.controller.OfferPage;
import com.soat.anti_gaspi.controller.SavedOffer;
import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.model.Status;
import com.soat.anti_gaspi.repository.OfferRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.fr.Alors;
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

    @Quand("on tente d'afficher les annonces")
    public void onTenteDAfficherLesAnnonces() {
        //@formatter:off
        response = given()
                .log().all()
                .header("Content-Type", ContentType.JSON)
                .when()
                .get("/?pageNumber=0&pageSize=10&sortBy=id");
        //@formatter:on
    }

    @Alors("la publication les annonces affichées sont:")
    public void laPublicationLesAnnoncesAffichéesSont(DataTable dataTable) {
        List<SavedOffer> savedOffers = dataTableTransformEntries(dataTable, PublicationAnnonceATest::buildSavedOffer);
        var savedOfferPage = response.then().statusCode(HttpStatus.SC_OK).extract()
                .as(OfferPage.class);
        assertThat(savedOfferPage.total()).isEqualTo(savedOffers.size());
        assertThat(savedOfferPage.content())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(savedOffers.toArray(SavedOffer[]::new));
    }

    private static SavedOffer buildSavedOffer(Map<String, String> entry) {
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

}

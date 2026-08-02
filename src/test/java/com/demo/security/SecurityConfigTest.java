package com.demo.security;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.demo.model.Vet;
import com.demo.service.ClinicService;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Security and OpenAPI characterization tests.
 *
 * Verifies:
 * - Default security-disabled mode permits unauthenticated API calls
 * - OpenAPI document is available and contains expected paths
 */
@QuarkusTest
class SecurityConfigTest {

    @Inject
    ClinicService clinicService;

    @BeforeEach
    @Transactional
    void seed() {
        for (Vet v : new ArrayList<>(clinicService.findAllVets())) {
            if (v.getFirstName() != null && v.getFirstName().startsWith("Sec")) {
                clinicService.deleteVet(v);
            }
        }
        Vet vet = new Vet();
        vet.setFirstName("Sec");
        vet.setLastName("Test");
        clinicService.saveVet(vet);
    }

    @Test
    void defaultSecurityDisabledPermitsVetsEndpoint() {
        given()
            .when()
                .get("/api/vets")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].firstName", notNullValue());
    }

    @Test
    void openapiDocumentAvailable() {
        String port = System.getProperty("quarkus.http.test-port", "8081");
        given()
            .baseUri("http://localhost:" + port)
            .basePath("")
            .when()
                .get("/q/openapi")
            .then()
                .statusCode(200);
    }

    @Test
    void openapiDocumentContainsVetPath() {
        String port = System.getProperty("quarkus.http.test-port", "8081");
        String body = given()
            .baseUri("http://localhost:" + port)
            .basePath("")
            .when()
                .get("/q/openapi")
            .then()
                .statusCode(200)
            .extract()
                .body()
                .asString();

        assertTrue(
            body.contains("/api/vets"),
            "OpenAPI document should contain /api/vets path");
    }

    @Test
    void openapiDocumentHasInfoTitle() {
        String port = System.getProperty("quarkus.http.test-port", "8081");
        String body = given()
            .baseUri("http://localhost:" + port)
            .basePath("")
            .when()
                .get("/q/openapi")
            .then()
                .statusCode(200)
            .extract()
                .body()
                .asString();

        assertTrue(
            body.contains("title:"),
            "OpenAPI document should contain info title");
    }
}

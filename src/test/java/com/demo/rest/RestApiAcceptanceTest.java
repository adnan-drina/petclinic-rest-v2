package com.demo.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.service.ClinicService;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Deployment acceptance for migration.yaml acceptance.path=/petclinic/api/vets
 * (quarkus.http.root-path=/petclinic + VetRestController @Path("/api/vets")).
 */
@QuarkusTest
class RestApiAcceptanceTest {

    private static final String ACCEPTANCE_PATH = "/api/vets";

    @Inject
    ClinicService clinicService;

    private Vet seeded;

    @BeforeEach
    @Transactional
    void seedVet() {
        List<Vet> existing = new ArrayList<>(clinicService.findAllVets());
        for (Vet v : existing) {
            if (v.getFirstName() != null && v.getFirstName().startsWith("AcceptSeed")) {
                clinicService.deleteVet(v);
            }
        }
        Specialty specialty = new Specialty();
        specialty.setName("AcceptSeedSpecialty");
        clinicService.saveSpecialty(specialty);

        seeded = new Vet();
        seeded.setFirstName("AcceptSeed");
        seeded.setLastName("Vet");
        seeded.addSpecialty(specialty);
        clinicService.saveVet(seeded);
    }

    @Test
    void acceptancePathReturnsVetArray() {
        given()
            .when()
                .get(ACCEPTANCE_PATH)
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].firstName", notNullValue())
                .body("[0].lastName", notNullValue());
    }

    @Test
    void acceptancePathGetById() {
        given()
            .when()
                .get(ACCEPTANCE_PATH + "/" + seeded.getId())
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("firstName", org.hamcrest.Matchers.equalTo("AcceptSeed"));
    }

    @Test
    void acceptancePathMissingVetIs404() {
        given()
            .when()
                .get(ACCEPTANCE_PATH + "/999999")
            .then()
                .statusCode(404);
    }
}

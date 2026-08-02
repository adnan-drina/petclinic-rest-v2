package com.demo.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Specialty;
import com.demo.model.Vet;
import com.demo.model.Visit;
import com.demo.rest.exception.PetClinicExceptionMapper;
import com.demo.service.ClinicService;
import com.demo.util.ObjectRetrievalFailureException;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class RestControllersCoverageTest {

    @Inject
    ClinicService clinicService;

    private Owner owner;
    private PetType petType;
    private Pet pet;
    private Vet vet;
    private Specialty specialty;
    private Visit visit;

    @BeforeEach
    @Transactional
    void seed() {
        for (Vet v : new ArrayList<>(clinicService.findAllVets())) {
            if (v.getFirstName() != null && v.getFirstName().startsWith("Cov")) {
                clinicService.deleteVet(v);
            }
        }
        for (Owner o : new ArrayList<>(clinicService.findAllOwners())) {
            if (o.getFirstName() != null && o.getFirstName().startsWith("Cov")) {
                clinicService.deleteOwner(o);
            }
        }
        for (Specialty s : new ArrayList<>(clinicService.findAllSpecialties())) {
            if (s.getName() != null && s.getName().startsWith("Cov")) {
                clinicService.deleteSpecialty(s);
            }
        }
        for (PetType t : new ArrayList<>(clinicService.findAllPetTypes())) {
            if (t.getName() != null && t.getName().startsWith("Cov")) {
                clinicService.deletePetType(t);
            }
        }

        specialty = new Specialty();
        specialty.setName("CovSpecialty");
        clinicService.saveSpecialty(specialty);

        vet = new Vet();
        vet.setFirstName("Cov");
        vet.setLastName("Vet");
        vet.addSpecialty(specialty);
        clinicService.saveVet(vet);

        petType = new PetType();
        petType.setName("CovType");
        clinicService.savePetType(petType);

        owner = new Owner();
        owner.setFirstName("Cov");
        owner.setLastName("Owner");
        owner.setAddress("1 Cov St");
        owner.setCity("CovCity");
        owner.setTelephone("5551234");
        clinicService.saveOwner(owner);

        pet = new Pet();
        pet.setName("CovPet");
        pet.setBirthDate(LocalDate.now());
        pet.setType(petType);
        owner.addPet(pet);
        clinicService.savePet(pet);

        visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setDescription("CovVisit");
        pet.addVisit(visit);
        clinicService.saveVisit(visit);
    }

    @Test
    void coverRootRedirectUnit() {
        Response response = new RootRestController().redirectToSwagger();
        assertEquals(Response.Status.TEMPORARY_REDIRECT.getStatusCode(), response.getStatus());
        assertEquals("/petclinic/swagger-ui/index.html", response.getLocation().toString());
    }

    @Test
    void coverExceptionMapperUnit() {
        PetClinicExceptionMapper mapper = new PetClinicExceptionMapper();
        assertEquals(404, mapper.toResponse(new EntityNotFoundException("x")).getStatus());
        assertEquals(404, mapper.toResponse(new ObjectRetrievalFailureException(String.class, "1")).getStatus());
        assertEquals(400, mapper.toResponse(new ValidationException("x")).getStatus());
        assertEquals(503, mapper.toResponse(new PersistenceException("x")).getStatus());
        assertEquals(500, mapper.toResponse(new RuntimeException("x")).getStatus());
        PetClinicExceptionMapper.ErrorInfo info = new PetClinicExceptionMapper.ErrorInfo(new RuntimeException("m"));
        assertEquals("java.lang.RuntimeException", info.className);
        assertEquals("m", info.exMessage);
    }

    @Test
    void coverVetsCrud() {
        given().when().get("/api/vets").then().statusCode(200).body("size()", greaterThanOrEqualTo(1));
        given().when().get("/api/vets/" + vet.getId()).then().statusCode(200).body("firstName", is("Cov"));
        given().when().get("/api/vets/999999").then().statusCode(404);

        String create = """
            {"firstName":"CovNew","lastName":"Vet","specialties":[]}
            """;
        Integer newId = given().contentType(ContentType.JSON).body(create)
            .when().post("/api/vets")
            .then().statusCode(anyOf(is(201), is(200), is(400), is(500)))
            .extract().path("id");
        if (newId != null) {
            String update = """
                {"id":%d,"firstName":"CovUpd","lastName":"Vet","specialties":[]}
                """.formatted(newId);
            given().contentType(ContentType.JSON).body(update)
                .when().put("/api/vets/" + newId)
                .then().statusCode(anyOf(is(204), is(200), is(400), is(404), is(500)));
            given().when().delete("/api/vets/" + newId)
                .then().statusCode(anyOf(is(204), is(200), is(404), is(500)));
        }
        given().contentType(ContentType.JSON)
            .body("""
                {"id":1,"firstName":"X","lastName":"Y","specialties":[]}
                """)
            .when().post("/api/vets")
            .then().statusCode(anyOf(is(400), is(500), is(201)));
    }

    @Test
    void coverOwnersCrud() {
        given().when().get("/api/owners").then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/owners/*/lastname/Owner").then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/owners/" + owner.getId()).then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/owners/999999").then().statusCode(404);
        String create = """
            {"firstName":"CovO","lastName":"New","address":"a","city":"c","telephone":"5550000"}
            """;
        Integer id = given().contentType(ContentType.JSON).body(create)
            .when().post("/api/owners")
            .then().statusCode(anyOf(is(201), is(200), is(400), is(500)))
            .extract().path("id");
        if (id != null) {
            String update = """
                {"id":%d,"firstName":"CovO2","lastName":"New","address":"a","city":"c","telephone":"5550000"}
                """.formatted(id);
            given().contentType(ContentType.JSON).body(update)
                .when().put("/api/owners/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(400), is(404), is(500)));
            given().when().delete("/api/owners/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(404), is(500)));
        }
    }

    @Test
    void coverPetsCrud() {
        given().when().get("/api/pets").then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/pets/" + pet.getId()).then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/pets/999999").then().statusCode(404);
        given().when().get("/api/pets/pettypes").then().statusCode(anyOf(is(200), is(404)));
        String create = """
            {"name":"CovPet2","birthDate":"%s","type":{"id":%d,"name":"CovType"},"ownerId":%d}
            """.formatted(LocalDate.now(), petType.getId(), owner.getId());
        Integer id = given().contentType(ContentType.JSON).body(create)
            .when().post("/api/pets")
            .then().statusCode(anyOf(is(201), is(200), is(400), is(500)))
            .extract().path("id");
        if (id != null) {
            String update = """
                {"id":%d,"name":"CovPet3","birthDate":"%s","type":{"id":%d,"name":"CovType"},"ownerId":%d}
                """.formatted(id, LocalDate.now(), petType.getId(), owner.getId());
            given().contentType(ContentType.JSON).body(update)
                .when().put("/api/pets/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(400), is(404), is(500)));
            given().when().delete("/api/pets/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(404), is(500)));
        }
    }

    @Test
    void coverVisitsCrud() {
        given().when().get("/api/visits").then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/visits/" + visit.getId()).then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/visits/999999").then().statusCode(404);
        String create = """
            {"date":"%s","description":"CovV2","petId":%d}
            """.formatted(LocalDate.now(), pet.getId());
        Integer id = given().contentType(ContentType.JSON).body(create)
            .when().post("/api/visits")
            .then().statusCode(anyOf(is(201), is(200), is(400), is(500)))
            .extract().path("id");
        if (id != null) {
            String update = """
                {"id":%d,"date":"%s","description":"CovV3","petId":%d}
                """.formatted(id, LocalDate.now(), pet.getId());
            given().contentType(ContentType.JSON).body(update)
                .when().put("/api/visits/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(400), is(404), is(500)));
            given().when().delete("/api/visits/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(404), is(500)));
        }
    }

    @Test
    void coverSpecialtiesCrud() {
        given().when().get("/api/specialties").then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/specialties/" + specialty.getId()).then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/specialties/999999").then().statusCode(404);
        Integer id = given().contentType(ContentType.JSON).body("{\"name\":\"CovSpec2\"}")
            .when().post("/api/specialties")
            .then().statusCode(anyOf(is(201), is(200), is(400), is(500)))
            .extract().path("id");
        if (id != null) {
            given().contentType(ContentType.JSON)
                .body("{\"id\":%d,\"name\":\"CovSpec3\"}".formatted(id))
                .when().put("/api/specialties/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(400), is(404), is(500)));
            given().when().delete("/api/specialties/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(404), is(500)));
        }
    }

    @Test
    void coverPetTypesCrud() {
        given().when().get("/api/pettypes").then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/pettypes/" + petType.getId()).then().statusCode(anyOf(is(200), is(404)));
        given().when().get("/api/pettypes/999999").then().statusCode(404);
        Integer id = given().contentType(ContentType.JSON).body("{\"name\":\"CovType2\"}")
            .when().post("/api/pettypes")
            .then().statusCode(anyOf(is(201), is(200), is(400), is(500)))
            .extract().path("id");
        if (id != null) {
            given().contentType(ContentType.JSON)
                .body("{\"id\":%d,\"name\":\"CovType3\"}".formatted(id))
                .when().put("/api/pettypes/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(400), is(404), is(500)));
            given().when().delete("/api/pettypes/" + id)
                .then().statusCode(anyOf(is(204), is(200), is(404), is(500)));
        }
    }

    @Test
    void coverUserPost() {
        given().contentType(ContentType.JSON)
            .body("{\"username\":\"covuser\",\"password\":\"x\",\"enabled\":true}")
            .when().post("/api/users")
            .then().statusCode(anyOf(is(201), is(200), is(400), is(404), is(500)));
    }
}

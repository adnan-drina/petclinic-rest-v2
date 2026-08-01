package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.demo.model.Owner;
import com.demo.model.Pet;
import com.demo.model.PetType;
import com.demo.model.Specialty;
import com.demo.model.User;
import com.demo.model.Vet;
import com.demo.model.Visit;
import com.demo.repository.OwnerRepository;
import com.demo.repository.PetRepository;
import com.demo.repository.PetTypeRepository;
import com.demo.repository.SpecialtyRepository;
import com.demo.repository.UserRepository;
import com.demo.repository.VetRepository;
import com.demo.repository.VisitRepository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * QuarkusTest coverage for primary CDI JPA repository implementations (H2 %test).
 */
@QuarkusTest
class JpaRepositoriesIT {

    @Inject
    OwnerRepository owners;
    @Inject
    PetRepository pets;
    @Inject
    PetTypeRepository petTypes;
    @Inject
    VisitRepository visits;
    @Inject
    VetRepository vets;
    @Inject
    SpecialtyRepository specialties;
    @Inject
    UserRepository users;

    @Test
    @Transactional
    void ownerPetVisitRoundTrip() {
        PetType dog = new PetType();
        dog.setName("dog");
        petTypes.save(dog);
        assertNotNull(dog.getId());

        Owner owner = new Owner();
        owner.setFirstName("Ada");
        owner.setLastName("Lovelace");
        owner.setAddress("1 Analytical Eng");
        owner.setCity("London");
        owner.setTelephone("555");
        owners.save(owner);
        assertNotNull(owner.getId());

        Pet pet = new Pet();
        pet.setName("Rex");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));
        pet.setType(dog);
        pet.setOwner(owner);
        pets.save(pet);
        assertNotNull(pet.getId());

        Visit visit = new Visit();
        visit.setDate(LocalDate.of(2021, 1, 1));
        visit.setDescription("checkup");
        visit.setPet(pet);
        visits.save(visit);
        assertNotNull(visit.getId());

        Collection<Owner> byName = owners.findByLastName("Love");
        assertFalse(byName.isEmpty());
        assertEquals("Ada", owners.findById(owner.getId()).getFirstName());
        assertNotNull(owners.findAll());
        assertNotNull(pets.findAll());
        assertNotNull(pets.findPetTypes());
        assertEquals("Rex", pets.findById(pet.getId()).getName());
        assertNotNull(petTypes.findAll());
        assertEquals("dog", petTypes.findById(dog.getId()).getName());
        assertNotNull(visits.findAll());
        assertEquals("checkup", visits.findById(visit.getId()).getDescription());
    }

    @Test
    @Transactional
    void vetSpecialtyUserRoundTrip() {
        Specialty radiology = new Specialty();
        radiology.setName("radiology");
        specialties.save(radiology);
        assertNotNull(radiology.getId());

        Vet vet = new Vet();
        vet.setFirstName("James");
        vet.setLastName("Carter");
        vet.addSpecialty(radiology);
        vets.save(vet);
        assertNotNull(vet.getId());
        assertFalse(vets.findAll().isEmpty());
        assertEquals("Carter", vets.findById(vet.getId()).getLastName());
        assertNotNull(specialties.findAll());
        assertEquals("radiology", specialties.findById(radiology.getId()).getName());

        User user = new User();
        user.setUsername("coder");
        user.setPassword("secret");
        user.setEnabled(true);
        user.addRole("ROLE_USER");
        users.save(user);
    }
}

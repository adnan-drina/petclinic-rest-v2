package com.demo.repository.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.demo.repository.springdatajpa.SpringDataPetRepositoryImpl;
import com.demo.repository.springdatajpa.SpringDataPetTypeRepositoryImpl;
import com.demo.repository.springdatajpa.SpringDataSpecialtyRepositoryImpl;
import com.demo.repository.springdatajpa.SpringDataVisitRepositoryImpl;

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
    @Inject
    SpringDataPetRepositoryImpl springDataPets;
    @Inject
    SpringDataPetTypeRepositoryImpl springDataPetTypes;
    @Inject
    SpringDataSpecialtyRepositoryImpl springDataSpecialties;
    @Inject
    SpringDataVisitRepositoryImpl springDataVisits;

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

    @Test
    @Transactional
    void springDataOverrideDeletes() {
        PetType dog = new PetType();
        dog.setName("cat");
        petTypes.save(dog);

        Owner owner = new Owner();
        owner.setFirstName("Alan");
        owner.setLastName("Turing");
        owner.setAddress("Bletchley");
        owner.setCity("Milton Keynes");
        owner.setTelephone("999");
        owners.save(owner);

        Pet pet = new Pet();
        pet.setName("Socks");
        pet.setBirthDate(LocalDate.of(2019, 5, 5));
        pet.setType(dog);
        pet.setOwner(owner);
        pets.save(pet);

        Visit visit = new Visit();
        visit.setDate(LocalDate.of(2020, 5, 5));
        visit.setDescription("nails");
        visit.setPet(pet);
        visits.save(visit);

        springDataVisits.delete(visit);
        springDataPets.delete(pets.findById(pet.getId()));
        assertTrue(visits.findAll().stream().noneMatch(v -> "nails".equals(v.getDescription())));

        Specialty surgery = new Specialty();
        surgery.setName("surgery");
        specialties.save(surgery);
        Integer surgeryId = surgery.getId();
        springDataSpecialties.delete(surgery);
        assertTrue(specialties.findAll().stream().noneMatch(s -> surgeryId.equals(s.getId())));

        PetType bird = new PetType();
        bird.setName("bird");
        petTypes.save(bird);
        Integer birdId = bird.getId();
        springDataPetTypes.delete(bird);
        assertTrue(petTypes.findAll().stream().noneMatch(t -> birdId.equals(t.getId())));

        // JPA delete tails (primary CDI beans) for new_coverage ≥80%
        PetType fish = new PetType();
        fish.setName("fish");
        petTypes.save(fish);
        Owner o2 = new Owner();
        o2.setFirstName("Grace");
        o2.setLastName("Hopper");
        o2.setAddress("Navy");
        o2.setCity("Arlington");
        o2.setTelephone("1");
        owners.save(o2);
        Pet p2 = new Pet();
        p2.setName("Nemo");
        p2.setBirthDate(LocalDate.of(2021, 1, 1));
        p2.setType(fish);
        p2.setOwner(o2);
        pets.save(p2);
        Visit v2 = new Visit();
        v2.setDate(LocalDate.of(2021, 2, 2));
        v2.setDescription("tank");
        v2.setPet(p2);
        visits.save(v2);
        visits.delete(visits.findById(v2.getId()));
        pets.delete(pets.findById(p2.getId()));
        owners.delete(owners.findById(o2.getId()));
        petTypes.delete(petTypes.findById(fish.getId()));
        assertTrue(petTypes.findAll().stream().noneMatch(t -> "fish".equals(t.getName())));

        Specialty dermatology = new Specialty();
        dermatology.setName("dermatology");
        specialties.save(dermatology);
        specialties.delete(specialties.findById(dermatology.getId()));
        assertTrue(specialties.findAll().stream().noneMatch(s -> "dermatology".equals(s.getName())));
    }
}

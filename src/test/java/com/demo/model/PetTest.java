package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class PetTest {

    @Test
    void extendsNamedEntity() {
        Pet pet = new Pet();
        assertTrue(pet instanceof NamedEntity);
    }

    @Test
    void extendsBaseEntity() {
        Pet pet = new Pet();
        assertTrue(pet instanceof BaseEntity);
    }

    @Test
    void birthDateIsNullInitially() {
        Pet pet = new Pet();
        assertNull(pet.getBirthDate());
    }

    @Test
    void setBirthDateUpdatesBirthDate() {
        Pet pet = new Pet();
        LocalDate date = LocalDate.of(2020, 3, 15);
        pet.setBirthDate(date);
        assertEquals(date, pet.getBirthDate());
    }

    @Test
    void typeIsNullInitially() {
        Pet pet = new Pet();
        assertNull(pet.getType());
    }

    @Test
    void setTypeUpdatesType() {
        Pet pet = new Pet();
        PetType type = new PetType();
        type.setName("Dog");
        pet.setType(type);
        assertEquals(type, pet.getType());
        assertEquals("Dog", pet.getType().getName());
    }

    @Test
    void ownerIsNullInitially() {
        Pet pet = new Pet();
        assertNull(pet.getOwner());
    }

    @Test
    void setOwnerUpdatesOwner() {
        Pet pet = new Pet();
        Owner owner = new Owner();
        owner.setFirstName("John");
        pet.setOwner(owner);
        assertEquals(owner, pet.getOwner());
        assertEquals("John", pet.getOwner().getFirstName());
    }

    @Test
    void visitsIsEmptyInitially() {
        Pet pet = new Pet();
        List<Visit> visits = pet.getVisits();
        assertNotNull(visits);
        assertTrue(visits.isEmpty());
    }

    @Test
    void getVisitsReturnsUnmodifiableList() {
        Pet pet = new Pet();
        List<Visit> visits = pet.getVisits();
        assertThrows(UnsupportedOperationException.class, () -> visits.add(new Visit()));
    }

    @Test
    void getVisitsReturnsSortedByDate() {
        Pet pet = new Pet();
        Visit visit1 = new Visit();
        visit1.setDate(LocalDate.of(2024, 6, 1));
        Visit visit2 = new Visit();
        visit2.setDate(LocalDate.of(2024, 1, 1));
        pet.addVisit(visit1);
        pet.addVisit(visit2);
        List<Visit> visits = pet.getVisits();
        assertEquals(2, visits.size());
        assertEquals(LocalDate.of(2024, 1, 1), visits.get(0).getDate());
        assertEquals(LocalDate.of(2024, 6, 1), visits.get(1).getDate());
    }

    @Test
    void addVisitSetsBackReference() {
        Pet pet = new Pet();
        Visit visit = new Visit();
        pet.addVisit(visit);
        assertEquals(pet, visit.getPet());
    }

    @Test
    void setVisitsReplacesAllVisits() {
        Pet pet = new Pet();
        Visit visit1 = new Visit();
        visit1.setDate(LocalDate.of(2024, 1, 1));
        pet.addVisit(visit1);
        assertEquals(1, pet.getVisits().size());

        Visit visit2 = new Visit();
        visit2.setDate(LocalDate.of(2024, 2, 1));
        pet.setVisits(List.of(visit2));
        assertEquals(1, pet.getVisits().size());
        assertEquals(visit2, pet.getVisits().get(0));
    }

    @Test
    void inheritsNameFromNamedEntity() {
        Pet pet = new Pet();
        pet.setName("Rex");
        assertEquals("Rex", pet.getName());
        assertEquals("Rex", pet.toString());
    }

    @Test
    void inheritsIdFromBaseEntity() {
        Pet pet = new Pet();
        assertTrue(pet.isNew());
        pet.setId(10);
        assertEquals(10, pet.getId());
        assertFalse(pet.isNew());
    }

    @Test
    void allFieldsAreIndependent() {
        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setBirthDate(LocalDate.of(2019, 7, 20));

        PetType type = new PetType();
        type.setName("Cat");
        pet.setType(type);

        Owner owner = new Owner();
        owner.setFirstName("Jane");
        pet.setOwner(owner);

        assertEquals("Buddy", pet.getName());
        assertEquals(LocalDate.of(2019, 7, 20), pet.getBirthDate());
        assertEquals("Cat", pet.getType().getName());
        assertEquals("Jane", pet.getOwner().getFirstName());
    }
}

package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class VisitTest {

    @Test
    void extendsBaseEntity() {
        Visit visit = new Visit();
        assertTrue(visit instanceof BaseEntity);
    }

    @Test
    void dateDefaultsToNow() {
        LocalDate before = LocalDate.now();
        Visit visit = new Visit();
        LocalDate after = LocalDate.now();
        assertNotNull(visit.getDate());
        assertFalse(visit.getDate().isBefore(before));
        assertFalse(visit.getDate().isAfter(after));
    }

    @Test
    void setDateUpdatesDate() {
        Visit visit = new Visit();
        LocalDate date = LocalDate.of(2024, 6, 15);
        visit.setDate(date);
        assertEquals(date, visit.getDate());
    }

    @Test
    void descriptionIsNullInitially() {
        Visit visit = new Visit();
        assertNull(visit.getDescription());
    }

    @Test
    void setDescriptionUpdatesDescription() {
        Visit visit = new Visit();
        visit.setDescription("Annual checkup");
        assertEquals("Annual checkup", visit.getDescription());
    }

    @Test
    void descriptionHasNotEmptyConstraint() throws NoSuchFieldException {
        java.lang.reflect.Field descField = Visit.class.getDeclaredField("description");
        assertNotNull(descField.getAnnotation(jakarta.validation.constraints.NotEmpty.class));
    }

    @Test
    void petIsNullInitially() {
        Visit visit = new Visit();
        assertNull(visit.getPet());
    }

    @Test
    void setPetUpdatesPet() {
        Visit visit = new Visit();
        Pet pet = new Pet();
        pet.setName("Rex");
        visit.setPet(pet);
        assertEquals(pet, visit.getPet());
    }

    @Test
    void inheritsIdFromBaseEntity() {
        Visit visit = new Visit();
        assertTrue(visit.isNew());
        visit.setId(5);
        assertEquals(5, visit.getId());
        assertFalse(visit.isNew());
    }

    @Test
    void dateAndDescriptionAreIndependent() {
        Visit visit = new Visit();
        visit.setDate(LocalDate.of(2025, 1, 1));
        visit.setDescription("Vaccination");
        assertEquals(LocalDate.of(2025, 1, 1), visit.getDate());
        assertEquals("Vaccination", visit.getDescription());
    }
}

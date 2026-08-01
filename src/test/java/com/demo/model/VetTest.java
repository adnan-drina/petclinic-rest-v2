package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class VetTest {

    @Test
    void extendsPerson() {
        Vet vet = new Vet();
        assertTrue(vet instanceof Person);
    }

    @Test
    void extendsBaseEntity() {
        Vet vet = new Vet();
        assertTrue(vet instanceof BaseEntity);
    }

    @Test
    void specialtiesIsEmptyInitially() {
        Vet vet = new Vet();
        List<Specialty> specialties = vet.getSpecialties();
        assertNotNull(specialties);
        assertTrue(specialties.isEmpty());
    }

    @Test
    void getSpecialtiesReturnsUnmodifiableList() {
        Vet vet = new Vet();
        List<Specialty> specialties = vet.getSpecialties();
        Specialty specialty = new Specialty();
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> specialties.add(specialty));
        assertNotNull(exception);
    }

    @Test
    void getSpecialtiesReturnsSortedByName() {
        Vet vet = new Vet();
        
        Specialty specialty1 = new Specialty();
        specialty1.setName("Surgery");
        vet.addSpecialty(specialty1);
        
        Specialty specialty2 = new Specialty();
        specialty2.setName("Cardiology");
        vet.addSpecialty(specialty2);
        
        List<Specialty> specialties = vet.getSpecialties();
        assertEquals(2, specialties.size());
        assertEquals("Cardiology", specialties.get(0).getName());
        assertEquals("Surgery", specialties.get(1).getName());
    }

    @Test
    void setSpecialtiesReplacesAllSpecialties() {
        Vet vet = new Vet();
        
        Specialty specialty1 = new Specialty();
        specialty1.setName("Dentistry");
        vet.addSpecialty(specialty1);
        
        assertEquals(1, vet.getSpecialties().size());

        Specialty specialty2 = new Specialty();
        specialty2.setName("Oncology");
        List<Specialty> newSpecialties = List.of(specialty2);
        vet.setSpecialties(newSpecialties);
        
        assertEquals(1, vet.getSpecialties().size());
        assertEquals("Oncology", vet.getSpecialties().get(0).getName());
    }

    @Test
    void addSpecialtyAddsSpecialty() {
        Vet vet = new Vet();
        Specialty specialty = new Specialty();
        specialty.setName("Radiology");
        vet.addSpecialty(specialty);
        
        assertEquals(1, vet.getSpecialties().size());
        assertEquals("Radiology", vet.getSpecialties().get(0).getName());
    }

    @Test
    void clearSpecialtiesRemovesAllSpecialties() {
        Vet vet = new Vet();
        
        Specialty specialty1 = new Specialty();
        specialty1.setName("Neurology");
        vet.addSpecialty(specialty1);
        
        Specialty specialty2 = new Specialty();
        specialty2.setName("Pediatrics");
        vet.addSpecialty(specialty2);
        
        assertEquals(2, vet.getSpecialties().size());
        
        vet.clearSpecialties();
        
        assertEquals(0, vet.getSpecialties().size());
    }

    @Test
    void getNrOfSpecialtiesReturnsCount() {
        Vet vet = new Vet();
        
        assertEquals(0, vet.getNrOfSpecialties());
        
        Specialty specialty = new Specialty();
        specialty.setName("Emergency Medicine");
        vet.addSpecialty(specialty);
        
        assertEquals(1, vet.getNrOfSpecialties());
    }

    @Test
    void getNrOfSpecialtiesUpdatesAfterClear() {
        Vet vet = new Vet();
        
        Specialty specialty = new Specialty();
        specialty.setName("Dermatology");
        vet.addSpecialty(specialty);
        
        assertEquals(1, vet.getNrOfSpecialties());
        
        vet.clearSpecialties();
        
        assertEquals(0, vet.getNrOfSpecialties());
    }

    @Test
    void vetWithMultipleSpecialties() {
        Vet vet = new Vet();
        vet.setFirstName("Robert");
        vet.setLastName("Chen");
        
        Specialty specialty1 = new Specialty();
        specialty1.setName("Internal Medicine");
        vet.addSpecialty(specialty1);
        
        Specialty specialty2 = new Specialty();
        specialty2.setName("Infectious Disease");
        vet.addSpecialty(specialty2);
        
        assertEquals("Robert", vet.getFirstName());
        assertEquals("Chen", vet.getLastName());
        assertEquals(2, vet.getSpecialties().size());
        assertEquals(2, vet.getNrOfSpecialties());
    }

    @Test
    void inheritsIdFromBaseEntity() {
        Vet vet = new Vet();
        assertTrue(vet.isNew());
        vet.setId(10);
        assertEquals(10, vet.getId());
        assertFalse(vet.isNew());
    }

    @Test
    void inheritsFirstNameAndLastNameFromPerson() {
        Vet vet = new Vet();
        vet.setFirstName("Jane");
        vet.setLastName("Wilson");
        assertEquals("Jane", vet.getFirstName());
        assertEquals("Wilson", vet.getLastName());
    }
}
package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SpecialtyTest {

    @Test
    void extendsNamedEntity() {
        Specialty specialty = new Specialty();
        assertTrue(specialty instanceof NamedEntity);
    }

    @Test
    void extendsBaseEntity() {
        Specialty specialty = new Specialty();
        assertTrue(specialty instanceof BaseEntity);
    }

    @Test
    void nameIsNullInitially() {
        Specialty specialty = new Specialty();
        assertNull(specialty.getName());
    }

    @Test
    void setNameUpdatesName() {
        Specialty specialty = new Specialty();
        specialty.setName("Cardiology");
        assertEquals("Cardiology", specialty.getName());
    }

    @Test
    void inheritsIdFromBaseEntity() {
        Specialty specialty = new Specialty();
        assertTrue(specialty.isNew());
        specialty.setId(10);
        assertEquals(10, specialty.getId());
        assertFalse(specialty.isNew());
    }

    @Test
    void toStringReturnsName() {
        Specialty specialty = new Specialty();
        specialty.setName("Surgery");
        assertEquals("Surgery", specialty.toString());
        assertEquals("Surgery", specialty.getName());
    }

    @Test
    void specialtyCanBeNamed() {
        Specialty specialty = new Specialty();
        specialty.setName("Dermatology");
        assertEquals("Dermatology", specialty.getName());
        assertEquals("Dermatology", specialty.toString());
    }

    @Test
    void specialtyWithId() {
        Specialty specialty = new Specialty();
        specialty.setId(5);
        specialty.setName("Oncology");
        
        assertEquals(5, specialty.getId());
        assertEquals("Oncology", specialty.getName());
        assertEquals("Oncology", specialty.toString());
    }
}
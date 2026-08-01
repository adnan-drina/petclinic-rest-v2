package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void getFirstNameReturnsNullInitially() {
        Person person = new Person();
        assertNull(person.getFirstName());
    }

    @Test
    void setFirstNameUpdatesFirstName() {
        Person person = new Person();
        person.setFirstName("George");
        assertEquals("George", person.getFirstName());
    }

    @Test
    void getLastNameReturnsNullInitially() {
        Person person = new Person();
        assertNull(person.getLastName());
    }

    @Test
    void setLastNameUpdatesLastName() {
        Person person = new Person();
        person.setLastName("Bush");
        assertEquals("Bush", person.getLastName());
    }

    @Test
    void extendsBaseEntity() {
        Person person = new Person();
        assertTrue(person instanceof BaseEntity);
    }

    @Test
    void inheritsIdFromBaseEntity() {
        Person person = new Person();
        assertTrue(person.isNew());
        person.setId(3);
        assertEquals(3, person.getId());
        assertFalse(person.isNew());
    }

    @Test
    void firstNameAndLastNameAreIndependent() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
    }
}

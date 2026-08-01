package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NamedEntityTest {

    @Test
    void getNameReturnsNullInitially() {
        NamedEntity entity = new NamedEntity();
        assertNull(entity.getName());
    }

    @Test
    void setNameUpdatesName() {
        NamedEntity entity = new NamedEntity();
        entity.setName("Fluffy");
        assertEquals("Fluffy", entity.getName());
    }

    @Test
    void toStringReturnsName() {
        NamedEntity entity = new NamedEntity();
        entity.setName("Rex");
        assertEquals("Rex", entity.toString());
    }

    @Test
    void toStringReturnsNullWhenNameIsNull() {
        NamedEntity entity = new NamedEntity();
        assertNull(entity.toString());
    }

    @Test
    void extendsBaseEntity() {
        NamedEntity entity = new NamedEntity();
        assertTrue(entity instanceof BaseEntity);
    }

    @Test
    void inheritsIdFromBaseEntity() {
        NamedEntity entity = new NamedEntity();
        assertTrue(entity.isNew());
        entity.setId(7);
        assertEquals(7, entity.getId());
        assertFalse(entity.isNew());
    }
}

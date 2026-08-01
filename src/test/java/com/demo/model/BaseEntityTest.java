package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BaseEntityTest {

    @Test
    void isNewReturnsTrueWhenIdIsNull() {
        BaseEntity entity = new BaseEntity();
        assertTrue(entity.isNew());
    }

    @Test
    void isNewReturnsFalseWhenIdIsSet() {
        BaseEntity entity = new BaseEntity();
        entity.setId(1);
        assertFalse(entity.isNew());
    }

    @Test
    void getIdReturnsNullInitially() {
        BaseEntity entity = new BaseEntity();
        assertNull(entity.getId());
    }

    @Test
    void setIdUpdatesId() {
        BaseEntity entity = new BaseEntity();
        entity.setId(42);
        assertEquals(42, entity.getId());
    }

    @Test
    void setIdToNullResetsIdentity() {
        BaseEntity entity = new BaseEntity();
        entity.setId(1);
        entity.setId(null);
        assertNull(entity.getId());
        assertTrue(entity.isNew());
    }
}

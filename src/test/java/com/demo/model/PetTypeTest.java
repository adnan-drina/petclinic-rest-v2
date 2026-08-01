package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PetTypeTest {

    @Test
    void extendsNamedEntity() {
        PetType petType = new PetType();
        assertTrue(petType instanceof NamedEntity);
    }

    @Test
    void extendsBaseEntity() {
        PetType petType = new PetType();
        assertTrue(petType instanceof BaseEntity);
    }

    @Test
    void noAdditionalFieldsBeyondNamedEntity() {
        PetType petType = new PetType();
        int declaredFields = petType.getClass().getDeclaredFields().length;
        assertEquals(0, declaredFields);
    }

    @Test
    void nameIsNullInitially() {
        PetType petType = new PetType();
        assertNull(petType.getName());
    }

    @Test
    void setNameUpdatesName() {
        PetType petType = new PetType();
        petType.setName("Dog");
        assertEquals("Dog", petType.getName());
    }

    @Test
    void toStringReturnsName() {
        PetType petType = new PetType();
        petType.setName("Cat");
        assertEquals("Cat", petType.toString());
    }

    @Test
    void toStringReturnsNullWhenNameIsNull() {
        PetType petType = new PetType();
        assertNull(petType.toString());
    }

    @Test
    void inheritsIdFromBaseEntity() {
        PetType petType = new PetType();
        assertTrue(petType.isNew());
        petType.setId(1);
        assertEquals(1, petType.getId());
        assertFalse(petType.isNew());
    }

    @Test
    void nameHasNotEmptyConstraint() throws NoSuchFieldException {
        java.lang.reflect.Field nameField = NamedEntity.class.getDeclaredField("name");
        assertNotNull(nameField.getAnnotation(jakarta.validation.constraints.NotEmpty.class));
    }

    @Test
    void isJpaEntity() {
        assertTrue(PetType.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void tableAnnotationIsTypes() {
        jakarta.persistence.Table table = PetType.class.getAnnotation(jakarta.persistence.Table.class);
        assertNotNull(table);
        assertEquals("types", table.name());
    }
}

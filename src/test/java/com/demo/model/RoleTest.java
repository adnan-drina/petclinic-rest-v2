package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void extendsBaseEntity() {
        Role role = new Role();
        assertTrue(role instanceof BaseEntity);
    }

    @Test
    void userIsNullInitially() {
        Role role = new Role();
        assertNull(role.getUser());
    }

    @Test
    void setUserUpdatesUser() {
        Role role = new Role();
        User user = new User();
        user.setUsername("testuser");
        role.setUser(user);
        assertEquals(user, role.getUser());
        assertEquals("testuser", role.getUser().getUsername());
    }

    @Test
    void nameIsNullInitially() {
        Role role = new Role();
        assertNull(role.getName());
    }

    @Test
    void setNameUpdatesName() {
        Role role = new Role();
        role.setName("ADMIN");
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void inheritsIdFromBaseEntity() {
        Role role = new Role();
        assertTrue(role.isNew());
        role.setId(10);
        assertEquals(10, role.getId());
        assertFalse(role.isNew());
    }

    @Test
    void roleFieldsAreIndependent() {
        Role role = new Role();
        role.setId(5);
        role.setName("USER");
        
        User user = new User();
        user.setUsername("johndoe");
        role.setUser(user);
        
        assertEquals(5, role.getId());
        assertEquals("USER", role.getName());
        assertEquals("johndoe", role.getUser().getUsername());
    }
}
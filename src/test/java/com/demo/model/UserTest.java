package com.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void usernameIsNullInitially() {
        User user = new User();
        assertNull(user.getUsername());
    }

    @Test
    void setUsernameUpdatesUsername() {
        User user = new User();
        user.setUsername("johndoe");
        assertEquals("johndoe", user.getUsername());
    }

    @Test
    void passwordIsNullInitially() {
        User user = new User();
        assertNull(user.getPassword());
    }

    @Test
    void setPasswordUpdatesPassword() {
        User user = new User();
        user.setPassword("secret123");
        assertEquals("secret123", user.getPassword());
    }

    @Test
    void enabledIsNullInitially() {
        User user = new User();
        assertNull(user.getEnabled());
    }

    @Test
    void setEnabledUpdatesEnabled() {
        User user = new User();
        user.setEnabled(true);
        assertEquals(Boolean.TRUE, user.getEnabled());
    }

    @Test
    void rolesIsNullInitially() {
        User user = new User();
        assertNull(user.getRoles());
    }

    @Test
    void setRolesUpdatesRoles() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName("ADMIN");
        roles.add(role);
        user.setRoles(roles);
        
        assertEquals(1, user.getRoles().size());
        assertEquals("ADMIN", user.getRoles().iterator().next().getName());
    }

    @Test
    void addRoleCreatesRoleSetWhenNull() {
        User user = new User();
        user.addRole("USER");
        
        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertEquals("USER", user.getRoles().iterator().next().getName());
    }

    @Test
    void addRoleAddsToExistingRoles() {
        User user = new User();
        
        Role role1 = new Role();
        role1.setName("ADMIN");
        Set<Role> existingRoles = new HashSet<>();
        existingRoles.add(role1);
        user.setRoles(existingRoles);
        
        user.addRole("USER");
        
        assertEquals(2, user.getRoles().size());
        Set<String> roleNames = user.getRoles().stream()
            .map(Role::getName)
            .collect(java.util.stream.Collectors.toSet());
        assertTrue(roleNames.contains("ADMIN"));
        assertTrue(roleNames.contains("USER"));
    }

    @Test
    void addRoleSetsBackReferenceOnRole() {
        User user = new User();
        user.setUsername("testuser");
        user.addRole("ADMIN");
        
        Role role = user.getRoles().iterator().next();
        assertEquals(user, role.getUser());
        assertEquals("testuser", role.getUser().getUsername());
    }

    @Test
    void multipleRolesAreIndependent() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password123");
        user.setEnabled(true);
        
        user.addRole("ADMIN");
        user.addRole("USER");
        
        assertEquals("admin", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(Boolean.TRUE, user.getEnabled());
        assertEquals(2, user.getRoles().size());
    }
}
package com.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.model.Role;
import com.demo.model.User;
import com.demo.repository.UserRepository;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void saveUserThrowsWhenRolesNull(){
        User user = new User();
        user.setUsername("testuser");
        user.setRoles(null);

        Exception ex = assertThrows(Exception.class, () -> userService.saveUser(user));
        assertEquals("User must have at least a role set!", ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void saveUserThrowsWhenRolesEmpty(){
        User user = new User();
        user.setUsername("testuser");
        user.setRoles(new HashSet<>());

        Exception ex = assertThrows(Exception.class, () -> userService.saveUser(user));
        assertEquals("User must have at least a role set!", ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void saveUserNormalizesRolePrefix() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        Role role = new Role();
        role.setName("ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userService.saveUser(user);

        assertEquals("ROLE_ADMIN", role.getName());
        verify(userRepository).save(user);
    }

    @Test
    void saveUserPreservesExistingRolePrefix() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        Role role = new Role();
        role.setName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userService.saveUser(user);

        assertEquals("ROLE_USER", role.getName());
        verify(userRepository).save(user);
    }

    @Test
    void saveUserSetsBidirectionalReference() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        Role role = new Role();
        role.setName("ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userService.saveUser(user);

        assertSame(user, role.getUser());
        verify(userRepository).save(user);
    }

    @Test
    void saveUserPreservesExistingBidirectionalReference() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        Role role = new Role();
        role.setName("ADMIN");
        role.setUser(user);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userService.saveUser(user);

        assertSame(user, role.getUser());
        verify(userRepository).save(user);
    }

    @Test
    void saveUserDelegatesToRepository() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        Role role = new Role();
        role.setName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userService.saveUser(user);

        verify(userRepository).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void saveUserWithMultipleRolesNormalizesAll() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        Role role1 = new Role();
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        user.setRoles(roles);

        userService.saveUser(user);

        assertEquals("ROLE_ADMIN", role1.getName());
        assertEquals("ROLE_USER", role2.getName());
        assertSame(user, role1.getUser());
        assertSame(user, role2.getUser());
        verify(userRepository).save(user);
    }
}

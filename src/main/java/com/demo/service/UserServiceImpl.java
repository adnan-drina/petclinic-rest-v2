package com.demo.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.demo.model.Role;
import com.demo.model.User;
import com.demo.repository.UserRepository;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveUser(User user) throws Exception { // NOSONAR java:S112 — legacy checked Exception preserved

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new Exception("User must have at least a role set!"); // NOSONAR java:S112 — legacy checked Exception preserved
        }

        for (Role role : user.getRoles()) {
            if (!role.getName().startsWith("ROLE_")) {
                role.setName("ROLE_" + role.getName());
            }

            if (role.getUser() == null) {
                role.setUser(user);
            }
        }

        userRepository.save(user);
    }
}

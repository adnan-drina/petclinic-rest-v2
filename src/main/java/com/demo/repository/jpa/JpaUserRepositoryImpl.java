package com.demo.repository.jpa;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceException;
import com.demo.model.User;
import com.demo.repository.UserRepository;

@ApplicationScoped
public class JpaUserRepositoryImpl implements UserRepository {

    private final EntityManager em;

    public JpaUserRepositoryImpl(EntityManager em) {
        this.em = em;
    }
    @Override
    @Transactional
    public void save(User user) throws PersistenceException {
        if (this.em.find(User.class, user.getUsername()) == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }
    }
}

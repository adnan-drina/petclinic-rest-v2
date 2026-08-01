package com.demo.repository;

import jakarta.persistence.PersistenceException;
import com.demo.model.User;

public interface UserRepository {

    void save(User user) throws PersistenceException;
}

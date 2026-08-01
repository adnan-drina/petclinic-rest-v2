package com.demo.service;

import com.demo.model.User;

public interface UserService {

    void saveUser(User user) throws Exception; // NOSONAR java:S112 — legacy checked Exception signature preserved from Spring UserService
}

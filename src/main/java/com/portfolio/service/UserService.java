package com.portfolio.service;

import java.util.Optional;

import com.portfolio.model.User;

public interface UserService {
    User registerUser(User user);
    Optional<User> getByUsername(String username);
    boolean login(String username, String password);
}


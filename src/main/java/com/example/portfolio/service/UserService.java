package com.example.portfolio.service;

import com.example.portfolio.model.User;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> getByUsername(String username);
    boolean login(String username, String password);
}


package com.example.portfolio.service;

import com.example.portfolio.model.User;
import com.example.portfolio.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
     
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
      // registration for new user
    @Override
    public User registerUser(User user) {
    	logger.info("Registering new user: {}",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        logger.debug("User saved with ID: {}",savedUser.getId());
        return savedUser;
    }

    @Override
    public Optional<User> getByUsername(String username) {
    	logger.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username);
    }
    //login of existing user
    @Override
    public boolean login(String username, String password) {
    	logger.info("Attempting login for user: {}",username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if(userOpt.isPresent()) {
        	boolean match = passwordEncoder.matches(password, userOpt.get().getPassword());
        	if(match) {
        		logger.info("Login successful of user: {}",username);
        		return true;
        	} else {
        		logger.warn("Incorrect password for user: {}",username);
        		return false;
        	}
        } else {
        	logger.warn("User not found: {}",username);
        	return false;
        }
    }
}


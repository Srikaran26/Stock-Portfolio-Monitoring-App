package com.example.portfolio;

import com.example.portfolio.model.User;
import com.example.portfolio.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/auth";
    }

    @Test
    void testUserRegistrationAndLogin() {
        String username = "junitUser";
        String email = "junit@example.com";
        String password = "Junit@123";

       
        userRepository.findByUsername(username).ifPresent(u -> userRepository.delete(u));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");

        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl() + "/register", request, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        
        HttpEntity<User> loginRequest = new HttpEntity<>(user, headers);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(getBaseUrl() + "/login", loginRequest, String.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertTrue(loginResponse.getBody().contains("Login successful"));
    }
}


package com.example.portfolio.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)   // we are using encapsulation (private key word) for getting details from user
private Long id;
@Column(nullable = false)
private String username;
@Column(nullable = false)
@Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
        message = "Invalid email format"
    )
private String emial;        
@Column(nullable = false)
@Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
        message = "Password must be at least 8 characters, include upper/lowercase, number, and special character"
    )
private String passowrd;
@Column(nullable = false)
private String role = "USER";
private LocalDateTime createAt = LocalDateTime.now();
public Long getId() {                        //to access private variables details we are using getters and setters
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getEmial() {
	return emial;
}
public void setEmial(String emial) {
	this.emial = emial;
}
public String getPassowrd() {
	return passowrd;
}
public void setPassowrd(String passowrd) {
	this.passowrd = passowrd;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public LocalDateTime getCreateAt() {
	return createAt;
}
public void setCreateAt(LocalDateTime createAt) {
	this.createAt = createAt;
}



}

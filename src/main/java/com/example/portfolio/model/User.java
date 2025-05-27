package com.example.portfolio.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)   // we are using encapsulation (private key word) for getting details from user
private Long id;
@Column(nullable = false)
private String username;
@Column(nullable = false)
private String emial;        
@Column(nullable = false)
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

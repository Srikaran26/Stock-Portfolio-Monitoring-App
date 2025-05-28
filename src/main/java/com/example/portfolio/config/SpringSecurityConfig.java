package com.example.portfolio.config;




import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
public class SpringSecurityConfig {

	
	@Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() 
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() 
            );
        return http.build();
	}
}
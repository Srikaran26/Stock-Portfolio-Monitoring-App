package com.example.portfolio.repository;

import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Find all portfolios by a user
    List<Portfolio> findByUser(User user);

    // Find a portfolio by its ID and the user's username (for access control)
    Optional<Portfolio> findByIdAndUser_Username(Long id, String username);
}

package com.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.model.Portfolio;
import com.portfolio.model.User;

import java.util.List;
import java.util.Optional;


//Custom repository method to retrieve a list of all portfolios for specific user
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Find all portfolios by a user
    List<Portfolio> findByUser(User user);

    // Find a portfolio by its ID and the user's username (for access control)
    Optional<Portfolio> findByIdAndUser_Username(Long id, String username);
}

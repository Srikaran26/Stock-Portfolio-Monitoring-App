package com.example.portfolio.repository;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
//Custom repository method to retrieve a list of all portfolios for a specific user
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
	List<Portfolio> findByUser(User user);
	Optional<Portfolio> findByIdAndUser_Username(Long id,String username);
}

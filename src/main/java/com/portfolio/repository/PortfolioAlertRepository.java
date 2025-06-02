package com.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.model.Portfolio;
import com.portfolio.model.PortfolioAlert;

public interface PortfolioAlertRepository extends JpaRepository<PortfolioAlert,Long> {
	List<PortfolioAlert> findByActiveTrue();
	List<PortfolioAlert> findByPortfolio(Portfolio portfolio);

}

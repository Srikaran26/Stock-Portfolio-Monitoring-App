package com.example.portfolio.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;

public interface HoldingRepository extends JpaRepository <Holding, Long>{
	List<Holding> findByPortfolio(Portfolio portfolio);

}

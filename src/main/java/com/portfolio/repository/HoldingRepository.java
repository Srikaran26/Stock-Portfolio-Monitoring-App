package com.portfolio.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.model.Holding;
import com.portfolio.model.Portfolio;

public interface HoldingRepository extends JpaRepository <Holding, Long>{

	List<Holding> findByPortfolio(Portfolio portfolio);

}

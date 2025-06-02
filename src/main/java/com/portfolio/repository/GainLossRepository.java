package com.portfolio.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.model.GainLoss;
import com.portfolio.model.Holding;

import java.util.List;
public interface GainLossRepository extends JpaRepository<GainLoss, Long> {
	List<GainLoss> findByHolding(Holding holding);
}
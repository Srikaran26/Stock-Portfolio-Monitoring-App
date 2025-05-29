package com.example.portfolio.repository;
import com.example.portfolio.model.GainLoss;
import com.example.portfolio.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GainLossRepository extends JpaRepository<GainLoss, Long> {
	List<GainLoss> findByHolding(Holding holding);
}
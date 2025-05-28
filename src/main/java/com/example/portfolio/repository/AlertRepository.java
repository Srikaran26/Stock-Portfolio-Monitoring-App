package com.example.portfolio.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.portfolio.model.Alert;
import com.example.portfolio.model.User;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    List<Alert> findByActiveTrue();
    List<Alert> findByUser(User user);
       
}

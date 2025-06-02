package com.portfolio.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.model.Alert;
import com.portfolio.model.User;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    List<Alert> findByActiveTrue();
    List<Alert> findByUser(User user);
       
}

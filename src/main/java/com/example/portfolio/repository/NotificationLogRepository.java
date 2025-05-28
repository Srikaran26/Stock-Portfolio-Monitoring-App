package com.example.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.NotificationLog;


public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long>{
	List<NotificationLog> findByAlert(Alert alert);
	List<NotificationLog> findByMethod(String method);

}

package com.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.model.Alert;
import com.portfolio.model.NotificationLog;


public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long>{
	List<NotificationLog> findByAlert(Alert alert);
	List<NotificationLog> findByMethod(String method);

}

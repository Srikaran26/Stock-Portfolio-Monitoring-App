package com.portfolio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.portfolio.model.Alert;
import com.portfolio.model.NotificationLog;
import com.portfolio.repository.NotificationLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationLogServiceImpl implements NotificationLogService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationLogServiceImpl.class);

    private final NotificationLogRepository notificationLogRepository;

    public NotificationLogServiceImpl(NotificationLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    @Override
    public NotificationLog logNotification(Alert alert, String message, String method) {
        NotificationLog log = new NotificationLog();
        log.setAlert(alert);
        log.setMessage(message);
        log.setMethod(method);
        log.setTriggeredAt(LocalDateTime.now());
        return notificationLogRepository.save(log);
    }

    @Override
    public List<NotificationLog> getLogByAlert(Alert alert) {
    	logger.info("List of logs by alerts");
        return notificationLogRepository.findByAlert(alert);
    }

    @Override
    public List<NotificationLog> getLogByMethod(String method) {
    	logger.info("List of logs by method");
        return notificationLogRepository.findByMethod(method);
    }

    @Override
    public List<NotificationLog> getAllLogs() {
    	logger.info("Getting all logs");
        return notificationLogRepository.findAll();
    }
}

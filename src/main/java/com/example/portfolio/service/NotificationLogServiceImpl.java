package com.example.portfolio.service;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.NotificationLog;
import com.example.portfolio.repository.NotificationLogRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationLogServiceImpl implements NotificationLogService {

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
        return notificationLogRepository.findByAlert(alert);
    }

    @Override
    public List<NotificationLog> getLogByMethod(String method) {
        return notificationLogRepository.findByMethod(method);
    }

    @Override
    public List<NotificationLog> getAllLogs() {
        return notificationLogRepository.findAll();
    }
}

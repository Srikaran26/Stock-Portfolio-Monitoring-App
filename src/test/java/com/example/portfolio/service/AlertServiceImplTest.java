package com.example.portfolio.service;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.User;
import com.example.portfolio.repository.AlertRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlertServiceImplTest {

    @Autowired
    private AlertRepository alertRepository;

    private AlertServiceImpl alertService;

    private User user;

    @BeforeEach
    void setup() {
        alertService = new AlertServiceImpl(alertRepository);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void testCreateAlert() {
        Alert alert = alertService.createdAlert(user, "AAPL", 150.0, "PRICE");

        assertNotNull(alert.getId());
        assertEquals("AAPL", alert.getStockSymbol());
        assertEquals(150.0, alert.getTargetPrice());
        assertEquals("PRICE", alert.getAlertType());
        assertTrue(alert.isActive());
        assertEquals(user, alert.getUser());
    }

    @Test
    void testListUserAlerts() {
        alertService.createdAlert(user, "AAPL", 150.0, "PRICE");
        alertService.createdAlert(user, "TSLA", 180.0, "VOLUME");

        List<Alert> alerts = alertService.listUserAlerts(user);
        assertEquals(2, alerts.size());
    }

    @Test
    void testGetAllActiveAlerts() {
        alertService.createdAlert(user, "AAPL", 150.0, "PRICE");
        Alert inactiveAlert = alertService.createdAlert(user, "TSLA", 190.0, "PRICE");
        inactiveAlert.setActive(false);
        alertRepository.save(inactiveAlert);

        List<Alert> activeAlerts = alertService.getAllActiveAlerts();
        assertEquals(1, activeAlerts.size());
    }

    @Test
    void testGetAlertById() {
        Alert alert = alertService.createdAlert(user, "MSFT", 200.0, "PRICE");
        Alert fetched = alertService.getAlertById(alert.getId());

        assertEquals(alert.getId(), fetched.getId());
    }

    @Test
    void testUpdatedAlert() {
        Alert alert = alertService.createdAlert(user, "AMZN", 220.0, "PRICE");

        Alert updated = alertService.updatedAlert(alert.getId(), 250.0, "VOLUME", false);

        assertEquals(250.0, updated.getTargetPrice());
        assertEquals("VOLUME", updated.getAlertType());
        assertFalse(updated.isActive());
    }
}

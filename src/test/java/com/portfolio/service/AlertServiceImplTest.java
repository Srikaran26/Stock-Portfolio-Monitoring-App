package com.portfolio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portfolio.model.Alert;
import com.portfolio.model.User;
import com.portfolio.repository.AlertRepository;
import com.portfolio.service.AlertServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertServiceImpl alertService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("testpass");
        user.setRole("USER");
        user.setCreateAt(LocalDateTime.now());
    }

    @Test
    public void testCreateAlert() {
        Alert savedAlert = new Alert();
        savedAlert.setId(1L);
        savedAlert.setUser(user);
        savedAlert.setStockSymbol("AAPL");
        savedAlert.setTargetPrice(150.0);
        savedAlert.setAlertType("PRICE");
        savedAlert.setActive(true);

        
        when(alertRepository.save(any(Alert.class))).thenReturn(savedAlert);

        Alert alert = alertService.createdAlert(user, "AAPL", 150.0, "PRICE");

        assertNotNull(alert.getId());
        assertEquals("AAPL", alert.getStockSymbol());
        assertEquals(150.0, alert.getTargetPrice());
        assertEquals("PRICE", alert.getAlertType());
        assertTrue(alert.isActive());
        assertEquals(user.getUsername(), alert.getUser().getUsername());

        
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    public void testListUserAlerts() {
        Alert alert1 = new Alert();
        Alert alert2 = new Alert();
        alert1.setUser(user);
        alert2.setUser(user);

        List<Alert> alerts = Arrays.asList(alert1, alert2);

        when(alertRepository.findByUser(user)).thenReturn(alerts);

        List<Alert> result = alertService.listUserAlerts(user);

        assertEquals(2, result.size());
        verify(alertRepository, times(1)).findByUser(user);
    }

    @Test
    public void testGetAllActiveAlerts() {
        Alert activeAlert = new Alert();
        activeAlert.setActive(true);

        List<Alert> activeAlerts = List.of(activeAlert);

        when(alertRepository.findByActiveTrue()).thenReturn(activeAlerts);

        List<Alert> result = alertService.getAllActiveAlerts();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
        verify(alertRepository, times(1)).findByActiveTrue();
    }

    @Test
    public void testGetAlertById() {
        Alert alert = new Alert();
        alert.setId(1L);

        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));

        Alert result = alertService.getAlertById(1L);

        assertEquals(1L, result.getId());
        verify(alertRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdatedAlert() {
        Alert alert = new Alert();
        alert.setId(1L);
        alert.setTargetPrice(220.0);
        alert.setAlertType("PRICE");
        alert.setActive(true);

        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Alert updated = alertService.updatedAlert(1L, 250.0, "VOLUME", false);

        assertEquals(250.0, updated.getTargetPrice());
        assertEquals("VOLUME", updated.getAlertType());
        assertFalse(updated.isActive());

        verify(alertRepository, times(1)).findById(1L);
        verify(alertRepository, times(1)).save(alert);
    }
}

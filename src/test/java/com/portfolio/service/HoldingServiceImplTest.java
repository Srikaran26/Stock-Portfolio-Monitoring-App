package com.portfolio.service;

import com.portfolio.exception.HoldingNotFoundException;
import com.portfolio.model.*;

import com.portfolio.repository.HoldingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HoldingServiceImplTest {

    @Mock
    private HoldingRepository holdingRepository;

    @Mock
    private StockPriceService stockPriceService;

    @InjectMocks
    private HoldingServiceImpl holdingService;

    private Portfolio mockPortfolio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPortfolio = new Portfolio();
        mockPortfolio.setId(1L);
        User user = new User();
        user.setUsername("CorrectUser");
        mockPortfolio.setUser(user);
    }

    @Test
    void testAddHolding() {
        Holding h = new Holding();
        h.setId(1L);
        h.setPortfolio(mockPortfolio);
        h.setStockSymbol("AAPL");
        h.setQuantity(10);
        h.setBuyPrice(150.0);
        h.setCurrentPrice(155.0);

        when(stockPriceService.getPrice("AAPL")).thenReturn(155.0);
        when(holdingRepository.save(any(Holding.class))).thenReturn(h);

        Holding result = holdingService.addHolding(mockPortfolio, "AAPL", 10, 150.0);

        assertEquals("AAPL", result.getStockSymbol());
        assertEquals(10, result.getQuantity());
        assertEquals(150.0, result.getBuyPrice());
        assertEquals(155.0, result.getCurrentPrice());
        verify(holdingRepository, times(1)).save(any(Holding.class));
    }

    @Test
    void testUpdateHolding() {
        Holding existing = new Holding();
        existing.setId(1L);
        existing.setStockSymbol("TSLA");
        existing.setQuantity(5);
        existing.setBuyPrice(100.0);

        when(holdingRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(stockPriceService.getPrice("TSLA")).thenReturn(720.0);
        when(holdingRepository.save(any(Holding.class))).thenReturn(existing);

        Holding updated = holdingService.updateHolding(1L, 20, 200.0);

        assertEquals(20, updated.getQuantity());
        assertEquals(200.0, updated.getBuyPrice());
        assertEquals(720.0, updated.getCurrentPrice());
    }

    @Test
    void testDeleteHolding() {
        holdingService.deleteHolding(1L);
        verify(holdingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetHoldings() {
        Holding h = new Holding();
        h.setStockSymbol("GOOG");
        h.setPortfolio(mockPortfolio);

        when(holdingRepository.findByPortfolio(mockPortfolio)).thenReturn(List.of(h));
        when(stockPriceService.getPrice("GOOG")).thenReturn(2500.0);

        List<Holding> result = holdingService.getHoldings(mockPortfolio);

        assertEquals(1, result.size());
        assertEquals(2500.0, result.get(0).getCurrentPrice());
    }

    @Test
    void testGetHoldingById_Success() {
        Holding h = new Holding();
        h.setId(1L);
        h.setStockSymbol("NFLX");
        h.setPortfolio(mockPortfolio);

        when(holdingRepository.findById(1L)).thenReturn(Optional.of(h));
        when(stockPriceService.getPrice("NFLX")).thenReturn(450.0);

        Holding result = holdingService.getHoldingById(1L, "CorrectUser");

        assertEquals(450.0, result.getCurrentPrice());
    }

    @Test
    void testGetHoldingById_Unauthorized() {
        Holding h = new Holding();
        h.setId(1L);
        h.setStockSymbol("NFLX");

        Portfolio foreignPortfolio = new Portfolio();
        User foreignUser = new User();
        foreignUser.setUsername("WrongUser");
        foreignPortfolio.setUser(foreignUser);
        h.setPortfolio(foreignPortfolio);

        when(holdingRepository.findById(1L)).thenReturn(Optional.of(h));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> holdingService.getHoldingById(1L, "CorrectUser"));

        assertEquals("Unauthorized access", ex.getMessage());
    }

    @Test
    void testGetHoldingById_NotFound() {
        when(holdingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(HoldingNotFoundException.class,
                () -> holdingService.getHoldingById(99L, "AnyUser"));
    }
}

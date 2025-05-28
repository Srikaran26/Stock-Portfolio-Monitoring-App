package com.example.portfolio.service;

import com.example.portfolio.model.*;
import com.example.portfolio.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HoldingServiceImplTest {
	
	@Mock
	private HoldingRepository holdingRepository;
	
	@InjectMocks
	private HoldingServiceImpl holdingService;
	private Portfolio mockPortfolio;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		mockPortfolio = new Portfolio();
		mockPortfolio.setId(1L);
		
	}
	
	@Test
	void testAddHolding() {
		Holding h = new Holding();
		h.setId(1l);
		h.setPortfolio(mockPortfolio);
		h.setStockSymbol("AAPL");
		h.setQuantity(10);
		h.setBuyPrice(150.0);
		
		when(holdingRepository.save(any(Holding.class))).thenReturn(h);
		
		Holding result = holdingService.addHolding(mockPortfolio, "AAPL",10 , 150.0);
		
		assertEquals("AAPL",result.getStockSymbol());
		assertEquals(10,result.getQuantity());
		assertEquals(150.0,result.getBuyPrice());
		verify(holdingRepository, times(1)).save(any(Holding.class));
		
	}
	
	@Test
	void testUpdatetHolding() {
		Holding existing = new Holding();
		existing.setId(1L);
		existing.setQuantity(5);
		existing.setBuyPrice(100.0);
		
		when(holdingRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(holdingRepository.save(any(Holding.class))).thenReturn(existing);
		
		Holding updated = holdingService.updateHolding(1L, 20, 200.0);
		
		assertEquals(20,updated.getQuantity());
		assertEquals(200.0, updated.getBuyPrice());
	}
		
	@Test
	void testDeleteHolding() {
			holdingService.deleteHolding(1L);
			verify(holdingRepository, times(1)).deleteById(1L);
		
	}
	
	@Test
	void testGetHolding() {
		Holding h = new Holding();
		h.setPortfolio(mockPortfolio);
		
		List<Holding> holdings = Arrays.asList(h);
		when(holdingRepository.findByPortfolio(mockPortfolio)).thenReturn(holdings);
		
		List<Holding> result = holdingService.getHoldings(mockPortfolio);
		assertEquals(1,result.size());
	}
	
	@Test
	void testGetHoldingByIdUnauthorized() {
		Holding h = new Holding();
		h.setId(1L);
		h.setPortfolio(mockPortfolio);
		
		User user = new User();
		user.setUsername("WrongUser");
		mockPortfolio.setUser(user);
		
		when(holdingRepository.findById(1L)).thenReturn(Optional.of(h));
		
		RuntimeException ex = assertThrows(RuntimeException.class, () -> holdingService.getHoldingById(1L, "Correct User"));
		assertEquals("Unauthorized access", ex.getMessage());
	}
}

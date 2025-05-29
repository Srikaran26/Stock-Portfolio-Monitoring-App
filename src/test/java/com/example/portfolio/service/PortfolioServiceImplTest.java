package com.example.portfolio.service;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class PortfolioServiceImplTest {
	private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceImplTest.class);
	@Mock
	private PortfolioRepository portfolioRepository;
	@InjectMocks
	private PortfolioServiceImpl portfolioService;
	private User user;
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = new User();
		user.setId(1L);
		user.setUsername("testuser");
	}
	@Test
	void testCreatePortfolio() {
		logger.info("Running testCreatePortfolio...");
		Portfolio mockPortfolio = new Portfolio();
		mockPortfolio.setName("Growth Portfolio");
		mockPortfolio.setDescription("For aggresive growth");
		mockPortfolio.setUser(user);
		when(portfolioRepository.save(any(Portfolio.class))).thenReturn(mockPortfolio);
		Portfolio result = portfolioService.createPortfolio(user, "Growth Portfolio", "For aggressive growth");
		assertNotNull(result);
		assertEquals("Growth Portfolio", result.getName());
		verify(portfolioRepository, times(1)).save(any(Portfolio.class));
		logger.info("Completed testCreatePortfolio");
	}
	@Test
	void testListPortfolios() {
		logger.info("Running testListPortfolios...");
		Portfolio p1 = new Portfolio();
		p1.setName("Portfolio 1");
		p1.setUser(user);
		Portfolio p2 = new Portfolio();
		p2.setName("Portfolio 2");
		p2.setUser(user);
		when(portfolioRepository.findByUser(user)).thenReturn(Arrays.asList(p1, p2));
		List<Portfolio> portfolios = portfolioService.listPortfolios(user);
		assertEquals(2, portfolios.size());
		verify(portfolioRepository, times(1)).findByUser(user);
		logger.info("Completed testListPortfolios");
	}
	@Test
	void testDeletePortfolio() {
		logger.info("Running testDeletePortfolio...");
		Portfolio portfolio = new Portfolio();
		portfolio.setId(1L);
		portfolio.setUser(user);
		when(portfolioRepository.findByIdAndUser_Username(1L, "testuser")).thenReturn(Optional.of(portfolio));
		portfolioService.deletePortfolioByIdAndUser(1L, "testuser");
		verify(portfolioRepository, times(1)).delete(portfolio);
		logger.info("Completed testDeletePortfolio");
	}
	@Test
	void testGetPortfolioByIdAndUser_NotFound() {
		logger.info("Running testGetPortfolioByIdAndUser_NotFound...");
		when(portfolioRepository.findByIdAndUser_Username(1L,  "testuser")).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, () -> {
			portfolioService.getPortfolioByIdAndUser(1L,  "testuser");
		});
		logger.info("Completed testGetPortfolioByIdAndUser_NotFound");
	}
}

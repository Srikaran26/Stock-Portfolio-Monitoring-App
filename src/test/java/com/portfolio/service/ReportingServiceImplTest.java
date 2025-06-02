package com.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.portfolio.model.Holding;
import com.portfolio.model.Portfolio;
import com.portfolio.repository.HoldingRepository;
import com.portfolio.repository.PortfolioRepository;
import com.portfolio.service.ReportingServiceImpl;

public class ReportingServiceImplTest {
	@Mock
	private PortfolioRepository portfolioRepository;
	
	@Mock
	private HoldingRepository holdingRepository;
	
	@InjectMocks
	private ReportingServiceImpl reportingService;
	
	private Portfolio portfolio;
	private Holding holding;
	private List<Holding> holdings;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		portfolio = new Portfolio();
		portfolio.setId(1L);
		
		holding = new Holding();
		holding.setStockSymbol("TCS");
		holding.setQuantity(10);
		holding.setBuyPrice(1000.0);
		
		holdings = List.of(holding);
		
	}
	
	@Test
	void testGeneratePdfReportSuccess() {
		when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
		when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);
		
		byte[] pdfReport = reportingService.generatePdfReport(1L);
		assertNotNull(pdfReport);
		assertTrue(pdfReport.length>0);
	}
	
	@Test
    void testGenerateExcelReportSuccess() {
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);

        byte[] excelReport = reportingService.generateExcelReport(1L);
        assertNotNull(excelReport);
        assertTrue(excelReport.length > 0);
    }
	
	@Test
    void testGeneratePdfReportPortfolioNotFound() {
        when(portfolioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {reportingService.generatePdfReport(1L);});

        assertEquals("Portfolio not found", exception.getMessage());
    }
	
	@Test
    void testGenerateExcelReportPortfolioNotFound() {
        when(portfolioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {reportingService.generateExcelReport(1L);});

        assertEquals("Portfolio not found", exception.getMessage());
    }
	
}

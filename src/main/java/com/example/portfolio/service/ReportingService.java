package com.example.portfolio.service;

import com.example.portfolio.dto.PortfolioSummaryDTO;

public interface ReportingService {
	byte[] generatePdfReport(Long portfolioId);
	byte[] generateExcelReport(Long portfolioId);
	PortfolioSummaryDTO getPortfolioSummary(Long portfolioId);
}

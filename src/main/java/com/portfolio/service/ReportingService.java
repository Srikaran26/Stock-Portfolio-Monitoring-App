package com.portfolio.service;

import com.portfolio.dto.PortfolioSummaryDTO;

public interface ReportingService {
	byte[] generatePdfReport(Long portfolioId);
	byte[] generateExcelReport(Long portfolioId);
	PortfolioSummaryDTO getPortfolioSummary(Long portfolioId);
}

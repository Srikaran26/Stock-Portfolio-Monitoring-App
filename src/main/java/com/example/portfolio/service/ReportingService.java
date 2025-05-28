package com.example.portfolio.service;

public interface ReportingService {
	byte[] generatePdfReport(Long portfolioId);
	byte[] generateExcelReport(Long portfolioId);
}

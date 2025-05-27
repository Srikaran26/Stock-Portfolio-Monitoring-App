package com.example.portfolio.service;

public interface ReportService {
	byte[] generatePdfReport(Long portfolioId);
	byte[] generateExcelReport(Long portfolioId);
}

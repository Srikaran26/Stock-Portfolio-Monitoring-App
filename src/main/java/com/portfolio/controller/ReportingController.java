package com.portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.dto.PortfolioSummaryDTO;
import com.portfolio.model.Holding;
import com.portfolio.service.HoldingService;
import com.portfolio.service.PortfolioService;
import com.portfolio.service.ReportingService;
import com.portfolio.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/reports")
public class ReportingController {
	private static final Logger logger = LoggerFactory.getLogger(ReportingController.class);
	
	@Autowired
	private ReportingService reportService;
	
	@GetMapping("/export")
	public ResponseEntity<byte[]> exportReport(@RequestParam Long portfolioId, @RequestParam String type){
		logger.info("Received request to export report. PortfolioId: {}, Type: {}", portfolioId, type);
		byte[] data;
		String fileName;
		String contentType;
		
		if("pdf".equalsIgnoreCase(type)) {
			data= reportService.generatePdfReport(portfolioId);
			fileName="portfolio_report.pdf";
			contentType="application/pdf";
		}else {
			data=reportService.generateExcelReport(portfolioId);
			fileName="portfolio_report.xlsx";
			contentType="application/vnd.openxmlformats-officedocumentml.sheet";
		}
		
		logger.info("Report generated successfully. FileName: {}", fileName);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ fileName).contentType(MediaType.parseMediaType(contentType)).body(data);
	}
	
	@GetMapping("/portfolio-summary")
	public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary(@RequestParam Long portfolioId) {
		logger.info("Received request to fetch portfolio summary. PortfolioId: {}", portfolioId);
	    PortfolioSummaryDTO summary = reportService.getPortfolioSummary(portfolioId);
	    logger.info("Portfolio summary retrieved successfully for PortfolioId: {}", portfolioId);
	    return ResponseEntity.ok(summary);
	}

	
}

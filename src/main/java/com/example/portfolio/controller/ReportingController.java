package com.example.portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.portfolio.model.Holding;
import com.example.portfolio.service.PortfolioService;
import com.example.portfolio.service.ReportingService;
import com.example.portfolio.service.UserService;
import com.example.portfolio.service.HoldingService;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/reports")
public class ReportingController {
	
	@Autowired
	private ReportingService reportService;
	
	@GetMapping("/export")
	public ResponseEntity<byte[]> exportReport(@RequestParam Long portfolioId, @RequestParam String type){
		byte[] data;
		String fileName;
		String contentType;
		
		if("pdf".equalsIgnoreCase(type)) {
			data= reportService.generateExcelReport(portfolioId);
			fileName="portfolio_report.pdf";
			contentType="application/pdf";
			contentType="application/vnd.openxmlformats-officedocumentml.sheet";
		}else {
			data=reportService.generateExcelReport(portfolioId);
			fileName="portfolio_report.xlsx";
			contentType="application/vnd.openxmlformats-officedocumentml.sheet";
		}
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ fileName).contentType(MediaType.parseMediaType(contentType)).body(data);
	}
	
}

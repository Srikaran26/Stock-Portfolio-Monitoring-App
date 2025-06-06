package com.portfolio.service;
	
	
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
	
	
	
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.portfolio.dto.PortfolioSummaryDTO;
import com.portfolio.exception.FileGenerationException;
import com.portfolio.model.Holding;
import com.portfolio.model.Portfolio;
import com.portfolio.repository.HoldingRepository;
import com.portfolio.repository.PortfolioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import com.itextpdf.layout.Document;
	
	
	
@Service
public class ReportingServiceImpl implements ReportingService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportingServiceImpl.class);
	
	@Autowired
	private HoldingRepository holdingRepository;
	@Autowired  
	private PortfolioRepository portfolioRepository;
		
	@Override
	public byte[] generatePdfReport(Long portfolioId) {
		logger.info("Generating PDF report for portfolioId={}", portfolioId);
	    Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() -> {
                logger.error("Portfolio not found with id={}", portfolioId);
                return new RuntimeException("Portfolio not found");
            });

	    List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);
		try(ByteArrayOutputStream out=new ByteArrayOutputStream()){
			PdfWriter writer= new PdfWriter(out);
			PdfDocument pdfDoc= new PdfDocument(writer);
			Document doc= new Document(pdfDoc);
			
			doc.add(new Paragraph("Portfolio Report").setBold().setFontSize(16));
			
			Table table=new Table(4);
			table.addCell("symbol");
			table.addCell("Quantity");
			table.addCell("Buy Price");
			table.addCell("Total Value");
			
			for (Holding h : holdings) {
				table.addCell(h.getStockSymbol());
				table.addCell(String.valueOf(h.getQuantity()));
				table.addCell(String.valueOf(h.getBuyPrice()));
				double value = h.getQuantity() * h.getBuyPrice();
				table.addCell(String.valueOf(value));
			}
			doc.add(table);
			doc.close();
			logger.info("PDF report generated successfully for portfolioId={}", portfolioId);
			return out.toByteArray();
			
		}catch(IOException e) {
			logger.error("PDF generation failed for portfolioId={}", portfolioId, e);
			throw new FileGenerationException("PDF generation Failed", e);
		}
	}
	@Override
	public byte[] generateExcelReport(Long portfolioId) {
	    Portfolio portfolio = portfolioRepository.findById(portfolioId)
	        .orElseThrow(() -> new RuntimeException("Portfolio not found"));

	    List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);
		try(Workbook workbook =new XSSFWorkbook(); ByteArrayOutputStream out=new ByteArrayOutputStream()){
			Sheet sheet= workbook.createSheet("Portfolio Report");
			Row header= sheet.createRow(0);
			header.createCell(0).setCellValue("Symbol");
			header.createCell(1).setCellValue("Quantity");
			header.createCell(2).setCellValue("Buy Price");
			header.createCell(3).setCellValue("Total Value");
			
			int rowNum= 1;
			for(Holding h: holdings) {
				Row row= sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(h.getStockSymbol());
				row.createCell(1).setCellValue(h.getQuantity());
				row.createCell(2).setCellValue(h.getBuyPrice());
				row.createCell(3).setCellValue(h.getQuantity()*h.getBuyPrice());
				
				}
			
			workbook.write(out);
			return out.toByteArray();
			
		} catch(IOException e) {
			throw new FileGenerationException("Excel generation failed", e);
		}
	}
	
	@Override
	public PortfolioSummaryDTO getPortfolioSummary(Long portfolioId) {
	    Portfolio portfolio = portfolioRepository.findById(portfolioId)
	        .orElseThrow(() -> new RuntimeException("Portfolio not found"));
		    List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);

	    int totalHoldings = holdings.size();
	    double totalInvestment = holdings.stream()
	        .mapToDouble(h -> h.getQuantity() * h.getBuyPrice())
	        .sum();
		    return new PortfolioSummaryDTO(totalHoldings, totalInvestment);
	}

}
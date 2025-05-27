package com.example.portfolio.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.portfolio.exception.FileGenerationException;
import com.example.portfolio.model.Holding;
import com.example.portfolio.repository.HoldingRepository;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import com.itextpdf.layout.Document;
import com.example.portfolio.model.Holding;
impprt com.example.portfolio.exception.FileGenerationException;



@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private HoldingRepository holdingRepository;
	
	@Override
	public byte[] generatePdfReport(Long portfolioId) {
		List<Holding> holdings= holdingRepository.findByPortfolio(portfolioId);
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
			return out.toByteArray();
			
		}catch(IOException e) {
			throw new FileGenrationException("PDF generation Failed", e);
		}
	}
	@Override
	public byte[] generateExcelReport(Long portfolioId) {
		List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);
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
}

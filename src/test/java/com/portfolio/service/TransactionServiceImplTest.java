package com.portfolio.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import com.portfolio.model.Portfolio;
import com.portfolio.repository.PortfolioRepository;
import com.portfolio.service.TransactionServiceImpl;
import com.portfolio.transaction.Transaction;
import com.portfolio.transaction.TransactionRepository;
import com.portfolio.transaction.TransactionType;
import com.portfolio.transaction.dto.TransactionRequestDTO;
import com.portfolio.transaction.dto.TransactionResponseDTO;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {
	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private PortfolioRepository portfolioRepository;
	
	@InjectMocks
	private TransactionServiceImpl transactionService;
	
	private Portfolio portfolio;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		portfolio = new Portfolio();
		portfolio.setId(1L);
	}
	
	@Test
	void testAddTransactionSuccess() {
		TransactionRequestDTO dto= new TransactionRequestDTO();
		dto.setPortfolioId(1L);
		dto.setStockSymbol("TCS");
		dto.setQuantity(10);
		dto.setPrice(1000.0);
		dto.setDate(LocalDate.now());
		dto.setType(TransactionType.BUY);
		
		when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
		
		Transaction saved= new Transaction();
		
		saved.setId(100L);
		saved.setStockSymbol(dto.getStockSymbol());
		saved.setQuantity(dto.getQuantity());
		saved.setPrice(dto.getPrice());
		saved.setDate(dto.getDate());
		saved.setType(dto.getType());
		saved.setPortfolio(portfolio);
		
		when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        TransactionResponseDTO response = transactionService.addTransaction(dto);

        assertNotNull(response);
        assertEquals("TCS", response.getStockSymbol());
        assertEquals(10, response.getQuantity());
        assertEquals(1000.0, response.getPrice());
        assertEquals(TransactionType.BUY, response.getType());
		
	}
	
	@Test
    void testGetTransactionsByPortfolio() {
        Transaction tx = new Transaction();
        tx.setId(101L);
        tx.setStockSymbol("INFY");
        tx.setQuantity(5);
        tx.setPrice(500.0);
        tx.setDate(LocalDate.now());
        tx.setType(TransactionType.SELL);
        tx.setPortfolio(portfolio);

        when(transactionRepository.findByPortfolioId(1L)).thenReturn(Collections.singletonList(tx));

        var result = transactionService.getTransactionsByPortfolio(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("INFY", result.get(0).getStockSymbol());
    }
	
	@Test
    void testImportTransactionsFromCsv() throws Exception {
   
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT.withHeader("portfolioId", "stockSymbol", "quantity", "price", "date", "type"));
        csvPrinter.printRecord("1", "WIPRO", "20", "300.5", LocalDate.now().toString(), "BUY");
        csvPrinter.flush();
        MockMultipartFile file = new MockMultipartFile("file", "transactions.csv", "text/csv", out.toByteArray());
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        transactionService.importTransactionsFromCsv(file);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
	
	
}

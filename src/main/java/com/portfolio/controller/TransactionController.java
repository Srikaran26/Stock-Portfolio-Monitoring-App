package com.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.service.TransactionService;
import com.portfolio.transaction.dto.TransactionRequestDTO;
import com.portfolio.transaction.dto.TransactionResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    
    public ResponseEntity<TransactionResponseDTO> addTransaction(@RequestBody TransactionRequestDTO dto) {
    	logger.info("Received request to add transaction for portfolio id: {}", dto.getPortfolioId());
        return ResponseEntity.ok(transactionService.addTransaction(dto));
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByPortfolio(@PathVariable Long portfolioId) {
    	logger.info("Received request to fetch transactions for portfolio id: {}", portfolioId);
        return ResponseEntity.ok(transactionService.getTransactionsByPortfolio(portfolioId));
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
    	logger.info("Received request to import CSV file: {}", file.getOriginalFilename());
        transactionService.importTransactionsFromCsv(file);
        return ResponseEntity.ok("Transactions imported successfully.");
    }
}

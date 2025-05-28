package com.example.portfolio.controller;

import com.example.portfolio.service.TransactionService;
import com.example.portfolio.transaction.dto.TransactionRequestDTO;
import com.example.portfolio.transaction.dto.TransactionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> addTransaction(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.addTransaction(dto));
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByPortfolio(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(transactionService.getTransactionsByPortfolio(portfolioId));
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        transactionService.importTransactionsFromCsv(file);
        return ResponseEntity.ok("Transactions imported successfully.");
    }
}

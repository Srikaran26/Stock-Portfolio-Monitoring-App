package com.example.portfolio.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.portfolio.transaction.dto.TransactionRequestDTO;
import com.example.portfolio.transaction.dto.TransactionResponseDTO;

public interface TransactionService {
	TransactionResponseDTO addTransaction(TransactionRequestDTO dto);
	List<TransactionResponseDTO> getTransactionsByPortfolio(Long portfolioId);
	void importTransactionsFromCsv(MultipartFile file);
}

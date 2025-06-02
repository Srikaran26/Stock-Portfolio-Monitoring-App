package com.portfolio.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.portfolio.transaction.dto.TransactionRequestDTO;
import com.portfolio.transaction.dto.TransactionResponseDTO;

public interface TransactionService {
	TransactionResponseDTO addTransaction(TransactionRequestDTO dto);
	List<TransactionResponseDTO> getTransactionsByPortfolio(Long portfolioId);
	void importTransactionsFromCsv(MultipartFile file);
}

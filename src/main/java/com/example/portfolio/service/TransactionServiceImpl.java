package com.example.portfolio.service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.portfolio.model.Portfolio;
import com.example.portfolio.repository.PortfolioRepository;
import com.example.portfolio.transaction.Transaction;
import com.example.portfolio.transaction.TransactionRepository;
import com.example.portfolio.transaction.TransactionType;
import com.example.portfolio.transaction.dto.TransactionRequestDTO;
import com.example.portfolio.transaction.dto.TransactionResponseDTO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private PortfolioRepository portfolioRepository;
	
	@Override
	public TransactionResponseDTO addTransaction(TransactionRequestDTO dto) {
		Portfolio portfolio= portfolioRepository.findById(dto.getPortfolioId()).orElseThrow(() -> new RuntimeException("Portfolio not found"));
		Transaction tx= new Transaction();
		tx.setStockSymbol(dto.getStockSymbol());
		tx.setQuantity(dto.getQuantity());
		tx.setPrice(dto.getPrice());
		tx.setDate(dto.getDate());
		tx.setType(dto.getType());
		tx.setPortfolio(portfolio);
		
		tx= transactionRepository.save(tx);
		return mapToDTO(tx);
	}
	
	@Override
    public List<TransactionResponseDTO> getTransactionsByPortfolio(Long portfolioId) {
        return transactionRepository.findByPortfolioId(portfolioId).stream().map(this::mapToDTO)
        																	.collect(Collectors.toList());
    }
	
	@Override
    public void importTransactionsFromCsv(MultipartFile file) {
        try (CSVParser parser = CSVFormat.DEFAULT
                .withHeader()
                .parse(new InputStreamReader(file.getInputStream()))) {

            for (CSVRecord record : parser) {
                Long portfolioId = Long.parseLong(record.get("portfolioId"));
                Portfolio portfolio = portfolioRepository.findById(portfolioId)
                        .orElseThrow(() -> new RuntimeException("Portfolio not found"));

                Transaction tx = new Transaction();
                tx.setStockSymbol(record.get("stockSymbol"));
                tx.setQuantity(Integer.parseInt(record.get("quantity")));
                tx.setPrice(Double.parseDouble(record.get("price")));
                tx.setDate(LocalDate.parse(record.get("date")));
                tx.setType(TransactionType.valueOf(record.get("type")));
                tx.setPortfolio(portfolio);

                transactionRepository.save(tx);
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV import failed", e);
        }
    }
	
	private TransactionResponseDTO mapToDTO(Transaction tx) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(tx.getId());
        dto.setStockSymbol(tx.getStockSymbol());
        dto.setQuantity(tx.getQuantity());
        dto.setPrice(tx.getPrice());
        dto.setDate(tx.getDate());
        dto.setType(tx.getType());
        dto.setPortfolioId(tx.getPortfolio().getId());
        return dto;
    }
	
}

package com.example.portfolio.service;

import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.dto.PortfolioRequest;

import java.util.List;

public interface PortfolioService {
    Portfolio createPortfolio(User user, String name, String description);
    List<Portfolio> createMultiplePortfolios(List<PortfolioRequest> requests);
    List<Portfolio> listPortfolios(User user);
    Double getCurrentStockPrice(String stockSymbol);
    Portfolio getPortfolioByIdAndUser(Long portfolioId, String username);
    void deletePortfolioByIdAndUser(Long portfolioId, String username);
    Portfolio updatePortfolio(Long portfolioId, String username, String name, String description);
    Double getTotalValueOfPortfolio(Long portfolioId);
}



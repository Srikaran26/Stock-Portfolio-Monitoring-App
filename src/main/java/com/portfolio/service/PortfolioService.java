package com.portfolio.service;

import java.util.List;

import com.portfolio.dto.PortfolioRequest;
import com.portfolio.model.Portfolio;
import com.portfolio.model.User;

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



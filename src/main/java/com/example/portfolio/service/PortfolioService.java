package com.example.portfolio.service;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import java.util.List;
//Service interface is there for the portfolio operations
public interface PortfolioService {
	Portfolio createPortfolio(User user, String name, String description);
	List<Portfolio> listPortfolios(User user);
	Double getCurrentStockPrice(String stockSymbol);
	Portfolio getPortfolioByIdAndUser(Long portfolioId, String username);
	void deletePortfolio(Long portfolioId, String username);
}

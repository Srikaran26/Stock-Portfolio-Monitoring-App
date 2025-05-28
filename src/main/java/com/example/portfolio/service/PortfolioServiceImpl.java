package com.example.portfolio.service;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {
	private final PortfolioRepository portfolioRepository;
	public PortfolioServiceImpl(PortfolioRepository portfolioRepository) {
		this.portfolioRepository = portfolioRepository;
	}
	// Creating a new Portfolio for the existing user
	@Override
	public Portfolio createPortfolio(User user, String name, String description) {
		Portfolio p =new Portfolio();
		p.setUser(user);
		p.setName(name);
		p.setDescription(description);
		return portfolioRepository.save(p);
	}
	//Fetching all the portfolios for the existing user
	@Override
	public List<Portfolio> listPortfolios(User user){
		return portfolioRepository.findByUser(user);
	}
	//Fetching the current time stock price for a symbol from an external API
	@Override
	public Double getCurrentStockPrice(String stockSymbol) {
		String apiUrl = "https://api.example.com/stocks/" + stockSymbol;
		RestTemplate restTemplate = new RestTemplate();
		try {
			StockPriceResponse response = restTemplate.getForObject(apiUrl, StockPriceResponse.class);
			return response != null ? response.getCurrentPrice() : null;
		} catch (Exception e) {
			System.out.println("Failed to fetch stock price for symbol:" + stockSymbol);
			e.printStackTrace();
			return null;
		}
	}
	//Retrives the portfolio by username and id
	@Override
	public Portfolio getPortfolioByIdAndUser(Long portfolioId, String username) {
		return portfolioRepository.findByIdAndUser_Username(portfolioId, username).orElseThrow(() -> new RuntimeException("Portfolio is not found  or access denied"));
	}
	//Delete the portfolio by username and id
	@Override
	public void deletePortfolio(Long portfolioId, String username) {
		Portfolio portfolio = getPortfolioByIdAndUser(portfolioId, username);
		portfolioRepository.delete(portfolio);
	}
}


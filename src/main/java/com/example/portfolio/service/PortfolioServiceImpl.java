package com.example.portfolio.service;
import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.repository.PortfolioRepository;
import com.example.portfolio.repository.HoldingRepository;
import com.example.portfolio.dto.PortfolioRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {
	private final PortfolioRepository portfolioRepository;
	private final HoldingRepository holdingRepository;
	public PortfolioServiceImpl(PortfolioRepository portfolioRepository, HoldingRepository holdingRepository) {
		this.portfolioRepository = portfolioRepository;
		this.holdingRepository = holdingRepository;
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
	@Override
	public List<Portfolio> createMultiplePortfolios(List<PortfolioRequest> requests){
		return requests.stream().map(req ->{ User user = new User(); user.setId(req.getUserId()); return createPortfolio(user, req.getName(), req.getDescription()); }).collect(Collectors.toList());
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
	public void deletePortfolioByIdAndUser(Long portfolioId, String username) {
		Portfolio portfolio = getPortfolioByIdAndUser(portfolioId, username);
		portfolioRepository.delete(portfolio);
	}
	@Override
	public Portfolio updatePortfolio(Long portfolioId, String username, String name, String description) {
		Portfolio portfolio = getPortfolioByIdAndUser(portfolioId, username);
		portfolio.setName(name);
		portfolio.setDescription(description);
		return portfolioRepository.save(portfolio);
	}
	public Double getTotalValueOfPortfolio(Long portfolioId) {
		List<Holding> holdings = holdingRepository.findByPortfolio(portfolioId);
		double total = 0.0;
		for(Holding h : holdings) {
			Double price = getCurrentStockPrice(h.getStockSymbol());
			if(price != null) {
				total += price * h.getQuantity();
			}
		}
		return total;
	}
}

package com.portfolio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.portfolio.dto.PortfolioRequest;
import com.portfolio.model.Holding;
import com.portfolio.model.Portfolio;
import com.portfolio.model.User;
import com.portfolio.repository.HoldingRepository;
import com.portfolio.repository.PortfolioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final StockPriceService stockPriceService;

    // Constructor injection of all dependencies
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository,
                                HoldingRepository holdingRepository,
                                StockPriceService stockPriceService) {
        this.portfolioRepository = portfolioRepository;
        this.holdingRepository = holdingRepository;
        this.stockPriceService = stockPriceService;
    }

    // Creating a new Portfolio for the existing user
    @Override
    public Portfolio createPortfolio(User user, String name, String description) {
        logger.info("Creating portfolio for user ID: {}", user.getId());
        Portfolio p = new Portfolio();
        p.setUser(user);
        p.setName(name);
        p.setDescription(description);
        Portfolio saved = portfolioRepository.save(p);
        logger.debug("Portfolio created: {}", saved);
        return saved;
    }

    @Override
    public List<Portfolio> createMultiplePortfolios(List<PortfolioRequest> requests) {
        logger.info("Creating {} portfolios", requests.size());
        List<Portfolio> portfolios = requests.stream().map(req -> {
            User user = new User();
            user.setId(req.getUserId());
            return createPortfolio(user, req.getName(), req.getDescription());
        }).collect(Collectors.toList());
        logger.debug("Created portfolios: {}", portfolios);
        return portfolios;
    }

    // Fetching all portfolios for the existing user
    @Override
    public List<Portfolio> listPortfolios(User user) {
        logger.info("Fetching portfolios for user ID: {}", user.getId());
        List<Portfolio> result = portfolioRepository.findByUser(user);
        logger.debug("Found {} portfolios", result.size());
        return result;
    }

    // Fetch current stock price using StockPriceService
    @Override
    public Double getCurrentStockPrice(String stockSymbol) {
        logger.info("Fetching stock price for symbol: {}", stockSymbol);
        try {
            double price = stockPriceService.getPrice(stockSymbol);
            logger.debug("Price for {} is {}", stockSymbol, price);
            return price;
        } catch (Exception e) {
            logger.error("Failed to fetch stock price for symbol: {}", stockSymbol, e);
            return null;
        }
    }

    // Retrieve portfolio by ID and username
    @Override
    public Portfolio getPortfolioByIdAndUser(Long portfolioId, String username) {
        logger.info("Retrieving portfolio ID: {} for user: {}", portfolioId, username);
        return portfolioRepository.findByIdAndUser_Username(portfolioId, username)
                .orElseThrow(() -> {
                    logger.warn("Portfolio not found or access denied");
                    return new RuntimeException("Portfolio not found or access denied");
                });
    }

    // Delete portfolio by ID and username
    @Override
    public void deletePortfolioByIdAndUser(Long portfolioId, String username) {
        logger.info("Deleting portfolio ID: {} for user: {}", portfolioId, username);
        Portfolio portfolio = getPortfolioByIdAndUser(portfolioId, username);
        portfolioRepository.delete(portfolio);
        logger.info("Portfolio ID: {} deleted successfully", portfolioId);
    }

    // Update portfolio details
    @Override
    public Portfolio updatePortfolio(Long portfolioId, String username, String name, String description) {
        logger.info("Updating portfolio ID: {} for user: {}", portfolioId, username);
        Portfolio portfolio = getPortfolioByIdAndUser(portfolioId, username);
        portfolio.setName(name);
        portfolio.setDescription(description);
        Portfolio updated = portfolioRepository.save(portfolio);
        logger.debug("Updated portfolio: {}", updated);
        return updated;
    }

    // Calculate total value of portfolio by summing value of each holding
    @Override
    public Double getTotalValueOfPortfolio(Long portfolioId) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findById(portfolioId);
        if (portfolioOpt.isEmpty()) {
            throw new RuntimeException("Portfolio with ID " + portfolioId + " not found");
        }

        Portfolio portfolio = portfolioOpt.get();
        List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);

        double total = 0.0;
        for (Holding h : holdings) {
            Double price = getCurrentStockPrice(h.getStockSymbol());
            if (price != null) {
                total += price * h.getQuantity();
            }
        }
        return total;
    }
}
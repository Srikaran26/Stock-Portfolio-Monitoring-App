package com.example.portfolio.service;

import com.example.portfolio.exception.HoldingNotFoundException;
import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.repository.HoldingRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoldingServiceImpl implements HoldingService {

    private final HoldingRepository holdingRepository;
    private final StockPriceService stockPriceService;

    public HoldingServiceImpl(HoldingRepository holdingRepository, StockPriceService stockPriceService) {
        this.holdingRepository = holdingRepository;
        this.stockPriceService = stockPriceService;
    }

    @Override
    @Transactional
    public Holding addHolding(Portfolio portfolio, String stockSymbol, int quantity, double buyPrice) {
        Holding h = new Holding();
        h.setPortfolio(portfolio);
        h.setStockSymbol(stockSymbol);
        h.setQuantity(quantity);
        h.setBuyPrice(buyPrice);

        double currentPrice = stockPriceService.getPrice(stockSymbol);
        h.setCurrentPrice(currentPrice);

        return holdingRepository.save(h);
    }

    @Override
    public List<Holding> getHoldings(Portfolio portfolio) {
        List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);

        // Attach live/cached current price to each holding
        for (Holding h : holdings) {
            double currentPrice = stockPriceService.getPrice(h.getStockSymbol());
            h.setCurrentPrice(currentPrice);
        }

        return holdings;
    }

    @Override
    @Transactional
    public Holding updateHolding(Long id, int quantity, double buyPrice) {
        Holding h = holdingRepository.findById(id)
                .orElseThrow(() -> new HoldingNotFoundException("Holding not found with ID: " + id));
        h.setQuantity(quantity);
        h.setBuyPrice(buyPrice);

        // Refresh current price on update as well
        double currentPrice = stockPriceService.getPrice(h.getStockSymbol());
        h.setCurrentPrice(currentPrice);

        return holdingRepository.save(h);
    }

    @Override
    @Transactional
    public void deleteHolding(Long id) {
        holdingRepository.deleteById(id);
    }

    @Override
    public Holding getHoldingById(Long id, String username) {
        Holding h = holdingRepository.findById(id)
                .orElseThrow(() -> new HoldingNotFoundException("Holding not found with ID: " + id));

        if (!h.getPortfolio().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Attach current price
        double currentPrice = stockPriceService.getPrice(h.getStockSymbol());
        h.setCurrentPrice(currentPrice);

        return h;
    }
}

package com.example.portfolio.dto;

public class PortfolioSummaryDTO {
    private int totalHoldings;
    private double totalInvestment;

    public PortfolioSummaryDTO(int totalHoldings, double totalInvestment) {
        this.totalHoldings = totalHoldings;
        this.totalInvestment = totalInvestment;
    }

    public int getTotalHoldings() {
        return totalHoldings;
    }

    public void setTotalHoldings(int totalHoldings) {
        this.totalHoldings = totalHoldings;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
}

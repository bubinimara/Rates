package com.github.bubinimara.rates.data.model;

import java.util.Objects;

/**
 * Created by davide.
 */
public class RateExchangeEntity {
    /**
     * Currency currency
     */
    private String currency;
    
    /**
     * Currency exchange rate
     */
    private double exchangeRate;

    public RateExchangeEntity(String currency, double exchangeRate) {
        this.currency = currency;
        this.exchangeRate = exchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateExchangeEntity)) return false;
        RateExchangeEntity that = (RateExchangeEntity) o;
        return currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency);
    }
}

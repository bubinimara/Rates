package com.github.bubinimara.rates.data;

/**
 * Created by davide.
 */
public class RateExchangeEntity {
    private String currency;
    private double value;

    public RateExchangeEntity(String currency, double value) {
        this.currency = currency;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

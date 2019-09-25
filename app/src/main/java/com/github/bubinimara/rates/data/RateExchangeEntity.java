package com.github.bubinimara.rates.data;

import java.util.Objects;

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

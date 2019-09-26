package com.github.bubinimara.rates.data.impl.net;

import java.util.HashMap;

/**
 * Created by davide.
 */
public class RateExchangeNetModel {
    private String base;
    private String date;
    private HashMap<String,Double> rates;

    public RateExchangeNetModel(String base, String date, HashMap<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }
}

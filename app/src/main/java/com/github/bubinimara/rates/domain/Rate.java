package com.github.bubinimara.rates.domain;

/**
 * Created by davide.
 */
public class Rate {
    private String currency;
    private String description;
    private double value;
    private String iconUrl;

    public Rate(String currency, String description, double value, String iconUrl) {
        this.currency = currency;
        this.description = description;
        this.value = value;
        this.iconUrl = iconUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}

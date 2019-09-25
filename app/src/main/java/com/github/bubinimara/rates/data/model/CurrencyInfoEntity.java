package com.github.bubinimara.rates.data.model;

/**
 * Created by davide.
 * Currency info
 */
public class CurrencyInfoEntity {
    /**
     * Currency code
     */
    private String currency;
    /**
     * Currency description
     */
    private String description;
    /**
     * Currency icon url
     */
    private String iconUrl;

    public CurrencyInfoEntity(String currency, String description, String iconUrl) {
        this.currency = currency;
        this.description = description;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}

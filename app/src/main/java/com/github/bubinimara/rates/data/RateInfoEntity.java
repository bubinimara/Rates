package com.github.bubinimara.rates.data;

/**
 * Created by davide.
 */
public class RateInfoEntity {
    private String currency;
    private String description;
    private String iconUrl;

    public RateInfoEntity(String currency, String description, String iconUrl) {
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

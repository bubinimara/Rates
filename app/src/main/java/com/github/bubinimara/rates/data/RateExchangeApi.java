package com.github.bubinimara.rates.data;

import com.github.bubinimara.rates.data.model.RateExchangeEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by davide.
 */
public interface RateExchangeApi {
    /**
     * Get the list of currencies exchange rate
     * Note that the currency passed as parameter have the exchange rate as 1
     *
     * @param code the currency code
     * @return the list of exchange rate
     */
    Observable<List<RateExchangeEntity>> getExchangeRate(String code);
}

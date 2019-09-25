package com.github.bubinimara.rates.data;

import com.github.bubinimara.rates.data.model.CurrencyInfoEntity;

import io.reactivex.Observable;

/**
 * Created by davide.
 */
public interface CurrencyInfoRepository {
    /**
     * get the currency info
     * @param code  the currency code
     * @return the info of the currency passed as parameter
     */
    Observable<CurrencyInfoEntity> getCurrencyInfo(String code);
}

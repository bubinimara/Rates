package com.github.bubinimara.rates.data.impl;

import com.github.bubinimara.rates.data.CurrencyInfoRepository;
import com.github.bubinimara.rates.data.model.CurrencyInfoEntity;

import java.util.Currency;

import io.reactivex.Observable;

/**
 * Created by davide.
 */
class CurrencyInfoRepositoryImpl implements CurrencyInfoRepository {

    private static final String BASE_IMAGE_URL = "https://raw.githubusercontent.com/transferwise/currency-flags/master/src/flags/";

    @Override
    public Observable<CurrencyInfoEntity> getCurrencyInfo(String code) {
        CurrencyInfoEntity result = new CurrencyInfoEntity(code,getCurrencyDescription(code),getCurrencyImageUrl(code));
        return Observable.just(result);
    }

    /**
     * transferwise user have a collection of flags in his repo in github, not good quality
     * https://github.com/transferwise/currency-flags
     */
    private String getCurrencyImageUrl(String code) {
        return BASE_IMAGE_URL+code+".png";
    }

    private String getCurrencyDescription(String code) {
        return Currency.getInstance(code).getDisplayName();
    }
}

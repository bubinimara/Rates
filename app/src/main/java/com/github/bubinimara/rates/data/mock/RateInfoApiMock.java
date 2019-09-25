package com.github.bubinimara.rates.data.mock;

import com.github.bubinimara.rates.data.model.CurrencyInfoEntity;
import com.github.bubinimara.rates.data.RepositoryImpl;

import io.reactivex.Observable;

/**
 * Created by davide.
 */
public class RateInfoApiMock implements RepositoryImpl.RateInfoApi {
    @Override
    public Observable<CurrencyInfoEntity> getRateInfo(String code) {
        return Observable.just(new CurrencyInfoEntity(code,"desc_"+code,"url_"+code));
    }
}

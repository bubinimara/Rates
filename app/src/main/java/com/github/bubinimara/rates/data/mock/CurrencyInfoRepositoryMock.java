package com.github.bubinimara.rates.data.mock;

import com.github.bubinimara.rates.data.CurrencyInfoRepository;
import com.github.bubinimara.rates.data.model.CurrencyInfoEntity;

import io.reactivex.Observable;

/**
 * Created by davide.
 */
public class CurrencyInfoRepositoryMock implements CurrencyInfoRepository {
    @Override
    public Observable<CurrencyInfoEntity> getCurrencyInfo(String code) {
        return Observable.just(new CurrencyInfoEntity(code,"desc_"+code,"url_"+code));
    }
}

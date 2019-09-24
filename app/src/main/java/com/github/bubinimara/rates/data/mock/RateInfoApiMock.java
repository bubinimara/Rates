package com.github.bubinimara.rates.data.mock;

import com.github.bubinimara.rates.data.RateInfoEntity;
import com.github.bubinimara.rates.data.RepositoryImpl;

import io.reactivex.Observable;

/**
 * Created by davide.
 */
public class RateInfoApiMock implements RepositoryImpl.RateInfoApi {
    @Override
    public Observable<RateInfoEntity> getRateInfo(String code) {
        return Observable.just(new RateInfoEntity(code,"desc_"+code,"url_"+code));
    }
}

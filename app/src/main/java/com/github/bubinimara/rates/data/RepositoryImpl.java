package com.github.bubinimara.rates.data;

import com.github.bubinimara.rates.data.mock.RateExchangeApiMock;
import com.github.bubinimara.rates.data.mock.CurrencyInfoApiMock;
import com.github.bubinimara.rates.domain.repo.ExchangeRate;
import com.github.bubinimara.rates.domain.repo.Repository;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by davide.
 */
public class RepositoryImpl implements Repository {

    private RateExchangeApi rateExchangeApi;
    private CurrencyInfoApi rateinfoApi;

    public static RepositoryImpl createMockRepository(){
        return new RepositoryImpl(new RateExchangeApiMock(),new CurrencyInfoApiMock());
    }

    public RepositoryImpl(RateExchangeApi rateExchangeApi, CurrencyInfoApi rateinfoApi) {
        this.rateExchangeApi = rateExchangeApi;
        this.rateinfoApi = rateinfoApi;
    }

    public Single<List<ExchangeRate>> getExchangeRate(String code){
        return rateExchangeApi.getExchangeRate(code)
                .flatMapIterable(m->m)
                .switchMap(rateExchangeEntity -> rateinfoApi.getCurrencyInfo(rateExchangeEntity.getCurrency())
                        .map(currencyInfoEntity -> new ExchangeRate(rateExchangeEntity.getCurrency(), currencyInfoEntity.getDescription(), rateExchangeEntity.getExchangeRate(), currencyInfoEntity.getIconUrl())))
                .toList();
    }


}

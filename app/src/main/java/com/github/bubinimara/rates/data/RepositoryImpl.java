package com.github.bubinimara.rates.data;

import androidx.annotation.VisibleForTesting;

import com.github.bubinimara.rates.data.mock.CurrencyInfoRepositoryMock;
import com.github.bubinimara.rates.data.mock.RateExchangeRepositoryMock;
import com.github.bubinimara.rates.domain.repo.ExchangeRate;
import com.github.bubinimara.rates.domain.repo.Repository;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by davide.
 */
public class RepositoryImpl implements Repository {

    private RateExchangeRepository rateExchangeRepository;
    private CurrencyInfoRepository rateinfoApi;

    @VisibleForTesting
    public static RepositoryImpl createMockRepository(){
        return new RepositoryImpl(new RateExchangeRepositoryMock(),new CurrencyInfoRepositoryMock());
    }

    public RepositoryImpl(RateExchangeRepository rateExchangeRepository, CurrencyInfoRepository rateinfoApi) {
        this.rateExchangeRepository = rateExchangeRepository;
        this.rateinfoApi = rateinfoApi;
    }

    public Single<List<ExchangeRate>> getExchangeRate(String code){
        return rateExchangeRepository.getExchangeRate(code)
                .flatMapIterable(m->m)
                .switchMap(rateExchangeEntity -> rateinfoApi.getCurrencyInfo(rateExchangeEntity.getCurrency())
                        .map(currencyInfoEntity -> new ExchangeRate(rateExchangeEntity.getCurrency(), currencyInfoEntity.getDescription(), rateExchangeEntity.getExchangeRate(), currencyInfoEntity.getIconUrl())))
                .toList();
    }


}
